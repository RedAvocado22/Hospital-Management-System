package com.hospital.hms.base.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a hypermedia link (HATEOAS - Hypermedia as the Engine of Application State)
 * <p>
 * HATEOAS makes your API self-documenting and discoverable.
 * Clients can navigate the API by following links rather than
 * hardcoding URLs.
 * <p>
 * Common relationship types:
 * - self: Link to the current resource
 * - next: Link to the next page (pagination)
 * - prev: Link to the previous page
 * - update: Link to update the resource
 * - delete: Link to delete the resource
 * - related: Link to related resources
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {

    /**
     * Relationship type
     * Examples: "self", "next", "prev", "update", "delete", "related"
     */
    private String rel;

    /**
     * The URL to the resource
     * Example: "/api/v1/courses/123"
     */
    private String href;

    /**
     * HTTP method to use with this link
     * Example: "GET", "POST", "PUT", "DELETE"
     */
    private String method;

    /**
     * Optional description of what this link does
     */
    private String description;

    /**
     * Create a simple link with just rel and href
     */
    public static Link of(String rel, String href) {
        return Link.builder()
                .rel(rel)
                .href(href)
                .build();
    }

    /**
     * Create a link with rel, href, and method
     */
    public static Link of(String rel, String href, String method) {
        return Link.builder()
                .rel(rel)
                .href(href)
                .method(method)
                .build();
    }
}
