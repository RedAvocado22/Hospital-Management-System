import os
import requests

GITHUB_TOKEN = os.environ["GITHUB_TOKEN"]
PR_NUMBER = os.environ["PR_NUMBER"]
REPO = os.environ["REPO"]

GH_HEADERS = {
    "Authorization": f"Bearer {GITHUB_TOKEN}",
    "Accept": "application/vnd.github+json",
}

SYSTEM_PROMPT = """You are a senior software engineer reviewing a pull request on a Spring Boot + React + MySQL project.

Focus on real issues only:
1. Correctness — logic errors, broken flows
2. Edge cases — null checks, empty collections, unhandled exceptions
3. Spring Boot issues — missing @Valid, N+1 queries, business logic in controllers, entities exposed directly from controllers instead of DTOs, wrong HTTP status codes
4. Security — missing auth checks, unvalidated input
5. Design — wrong layer responsibilities, services doing too much

Format your review as:

## 🔍 PR Review

### Summary
[1-2 sentence summary]

### Issues Found
**[File: ClassName.java, ~line N]**
- **Problem:** What's wrong and why it matters
- **Scenario:** A concrete case where this breaks
- **Suggestion:** Direction to fix, not the full solution

### What's Done Well
[Only if something genuinely stands out]

### Verdict
🔴 Needs changes / 🟡 Minor issues / 🟢 Looks good

Be direct. No padding."""


def get_pr_diff():
    url = f"https://api.github.com/repos/{REPO}/pulls/{PR_NUMBER}"
    headers = {**GH_HEADERS, "Accept": "application/vnd.github.v3.diff"}
    r = requests.get(url, headers=headers)
    r.raise_for_status()
    return r.text


def get_pr_metadata():
    url = f"https://api.github.com/repos/{REPO}/pulls/{PR_NUMBER}"
    r = requests.get(url, headers=GH_HEADERS)
    r.raise_for_status()
    d = r.json()
    return {
        "title": d["title"],
        "description": d.get("body") or "(no description)",
        "author": d["user"]["login"],
        "base": d["base"]["ref"],
        "head": d["head"]["ref"],
        "changed_files": d["changed_files"],
        "additions": d["additions"],
        "deletions": d["deletions"],
    }


def review_with_github_models(diff: str, metadata: dict) -> str:
    if len(diff) > 8_000:
        diff = diff[:8_000] + "\n\n... [diff truncated]"

    user_message = f"""
PR Title: {metadata['title']}
Author: {metadata['author']}
Branch: {metadata['head']} → {metadata['base']}
Stats: +{metadata['additions']} / -{metadata['deletions']} across {metadata['changed_files']} files

Description:
{metadata['description']}

Diff:
```diff
{diff}
```
""".strip()

    payload = {
        "model": "gpt-4o",
        "messages": [
            {"role": "system", "content": SYSTEM_PROMPT},
            {"role": "user", "content": user_message},
        ],
        "max_tokens": 2048,
    }

    r = requests.post(
        "https://models.inference.ai.azure.com/chat/completions",
        headers={
            "Authorization": f"Bearer {os.environ['GH_PAT']}",
            "Content-Type": "application/json",
        },
        json=payload,
    )
    r.raise_for_status()
    return r.json()["choices"][0]["message"]["content"]


def delete_old_reviews():
    url = f"https://api.github.com/repos/{REPO}/issues/{PR_NUMBER}/comments"
    r = requests.get(url, headers=GH_HEADERS)
    r.raise_for_status()
    for comment in r.json():
        if "<!-- auto-review -->" in comment.get("body", ""):
            requests.delete(
                f"https://api.github.com/repos/{REPO}/issues/comments/{comment['id']}",
                headers=GH_HEADERS,
            )


def post_comment(review: str):
    url = f"https://api.github.com/repos/{REPO}/issues/{PR_NUMBER}/comments"
    body = f"<!-- auto-review -->\n{review}\n\n---\n*🤖 Automated review by GitHub Models*"
    r = requests.post(url, headers=GH_HEADERS, json={"body": body})
    r.raise_for_status()
    print(f"✅ Posted: {r.json()['html_url']}")


if __name__ == "__main__":
    print("Fetching metadata...")
    metadata = get_pr_metadata()
    print("Fetching diff...")
    diff = get_pr_diff()
    print("Cleaning old comments...")
    delete_old_reviews()
    print("Reviewing with GitHub Models...")
    review = review_with_github_models(diff, metadata)
    print("Posting comment...")
    post_comment(review)
