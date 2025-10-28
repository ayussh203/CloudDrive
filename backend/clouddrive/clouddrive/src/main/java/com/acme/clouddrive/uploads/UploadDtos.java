package com.acme.clouddrive.uploads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UploadDtos {

    public static class PresignRequest {
        @NotBlank public String originalName;
        @NotBlank public String mimeType;
        @Min(0)  public long sizeBytes;
        public String checksumSha256; // optional
    }

    public static class PresignResponse {
        public String url;                 // presigned PUT URL
        public String key;                 // S3 object key to store/relate later
        public String requiredContentType; // must be sent when uploading

        public PresignResponse(String url, String key, String requiredContentType) {
            this.url = url;
            this.key = key;
            this.requiredContentType = requiredContentType;
        }
    }
}
