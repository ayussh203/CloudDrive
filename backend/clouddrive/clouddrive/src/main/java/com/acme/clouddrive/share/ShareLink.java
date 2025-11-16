package com.acme.clouddrive.share;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "share_links", indexes = {
    @Index(name="ix_share_code", columnList = "code", unique = true),
    @Index(name="ix_share_owner", columnList = "ownerId"),
    @Index(name="ix_share_file", columnList = "fileId")
})
public class ShareLink {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)  private Long ownerId;
    @Column(nullable = false)  private Long fileId;

    // short random code used in public path /s/{code}
    @Column(nullable = false, unique = true, length = 32)
    private String code;

    // optional vanity slug, unique per owner
    @Column(nullable = true, length = 64)
    private String slug;

    // bcrypt hash if password protected; null when open
    @Column(nullable = true, length = 120)
    private String passwordHash;

    // null = no expiry
    @Column(nullable = true)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate void onUpdate(){ this.updatedAt = Instant.now(); }

    // getters/setters…

    // --- boilerplate below (getters/setters) ---
    public Long getId() { return id; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public Long getFileId() { return fileId; }
    public void setFileId(Long fileId) { this.fileId = fileId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
