package com.acme.clouddrive.files;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class FileDtos {
    public static class CreateRequest {
        @NotBlank public String originalName;
        @NotBlank public String mimeType;
        @Min(0)  public long sizeBytes;
        public String checksumSha256; // optional
        public Long folderId; // optional

    }

    public static class UpdateRequest {
        @NotBlank public String originalName;
    }

    public static class FileResponse {
        public Long id;
        public String s3Key;
        public String originalName;
        public String mimeType;
        public long sizeBytes;
        public String checksumSha256;
        public Instant createdAt;
        public Integer version;

        public FileResponse(FileObject f) {
            this.id = f.getId();
            this.s3Key = f.getS3Key();
            this.originalName = f.getOriginalName();
            this.mimeType = f.getMimeType();
            this.sizeBytes = f.getSizeBytes();
            this.checksumSha256 = f.getChecksumSha256();
            this.createdAt = f.getCreatedAt();
             this.version = f.getVersion();
        }
    }
        public static class FileVersionResponse {
        public Long id;
        public int version;
        public String s3Key;
        public String mimeType;
        public long sizeBytes;
        public String checksumSha256;
        public Instant createdAt;

        public FileVersionResponse(FileVersion v) {
            this.id = v.getId();
            this.version = v.getVersion();
            this.s3Key = v.getS3Key();
            this.mimeType = v.getMimeType();
            this.sizeBytes = v.getSizeBytes();
            this.checksumSha256 = v.getChecksumSha256();
            this.createdAt = v.getCreatedAt();
        }
    }

}
