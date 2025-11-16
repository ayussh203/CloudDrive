package com.acme.clouddrive.files;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "files")
public class FileObject {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)          // owner user id (from our Users table)
    private Long ownerId;

    @Column(nullable = false, length = 512)
    private String s3Key;               // will be filled later (e.g., uploads/{userId}/{uuid}/name)

    @Column(nullable = false, length = 255)
    private String originalName;

    @Column(nullable = false, length = 120)
    private String mimeType;

     @Column(name = "folder_id")
    private Long folderId;   

    @Column(nullable = false)
    private long sizeBytes;

    @Column(length = 64)
    private String checksumSha256;      // optional

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public String getS3Key() { return s3Key; }
    public void setS3Key(String s3Key) { this.s3Key = s3Key; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(long sizeBytes) { this.sizeBytes = sizeBytes; }

    public String getChecksumSha256() { return checksumSha256; }
    public void setChecksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
      public Long getFolderId() { return folderId; }
    public void setFolderId(Long folderId) { this.folderId = folderId; } 
}
