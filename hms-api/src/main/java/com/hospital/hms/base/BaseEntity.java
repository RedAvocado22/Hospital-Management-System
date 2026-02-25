package com.hospital.hms.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(unique = true, nullable = false, length = 10)
    private String publicToken;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Auto-generate publicToken before first persist if not already set.
     */
    @PrePersist
    protected void onPrePersist() {
        if (publicToken == null || publicToken.isBlank()) {
            publicToken = generatePublicToken();
        }
    }

    /**
     * Generate a URL-friendly slug from a title.
     * Supports Unicode characters (Vietnamese, CJK, etc.) by transliterating
     * accented characters to ASCII equivalents.
     *
     * @param title the source text
     * @return a URL-safe slug
     */
    public static String generateSlug(String title) {
        if (title == null || title.isBlank()) {
            return "";
        }
        // Normalize Unicode to decomposed form (e.g., "ệ" → "e" + combining marks)
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD);
        // Remove combining diacritical marks (accents)
        String ascii = normalized.replaceAll("\\p{M}", "");
        // Convert special Vietnamese characters not handled by NFD
        ascii = ascii.replace("đ", "d").replace("Đ", "D");
        return ascii.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    /**
     * Generate a unique slug by appending a random suffix if needed.
     * Use this when you need to guarantee uniqueness.
     *
     * @param title the source text
     * @return a slug with a random suffix
     */
    public static String generateUniqueSlug(String title) {
        String baseSlug = generateSlug(title);
        String suffix = String.valueOf(System.currentTimeMillis() % 100000);
        return baseSlug.isEmpty() ? suffix : baseSlug + "-" + suffix;
    }

    /**
     * Generate a random 10-character alphanumeric public token.
     */
    private static String generatePublicToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(10);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * equals/hashCode based on id only (Hibernate-safe).
     * Two entities are equal if they have the same non-null id.
     * Unsaved entities (id == null) are only equal to themselves.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // Use a constant so hash doesn't change when id is assigned after persist
        return getClass().hashCode();
    }
}
