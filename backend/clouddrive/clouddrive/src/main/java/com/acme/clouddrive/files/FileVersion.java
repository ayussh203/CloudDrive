package com.acme.clouddrive.files;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "file_versions",
        indexes = {
                @Index(name = "idx_file_versions_file", columnList = "fileId, version")
        }
)
public class FileVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long fileId;

    @Column(nullable = false)
    private int version;

    @Column(nullable = false, length = 512)
    private String s3Key;

    @Column(nullable = false, length = 120)
    private String mimeType;

    @Column(nullable = false)
    private long sizeBytes;

    @Column(length = 64)
    private String checksumSha256;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public FileVersion() {
    }

    public static FileVersion fromFile(FileObject f, int version) {
        FileVersion v = new FileVersion();
        v.setFileId(f.getId());
        v.setVersion(version);
        v.setS3Key(f.getS3Key());
        v.setMimeType(f.getMimeType());
        v.setSizeBytes(f.getSizeBytes());
        v.setChecksumSha256(f.getChecksumSha256());
        return v;
    }

    public Long getId() {
        return id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public String getChecksumSha256() {
        return checksumSha256;
    }

    public void setChecksumSha256(String checksumSha256) {
        this.checksumSha256 = checksumSha256;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
