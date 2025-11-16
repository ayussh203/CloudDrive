package com.acme.clouddrive.share;

import java.time.Instant;

public class ShareDtos {
    public record CreateRequest(
            Long fileId,
            String slug,          // optional vanity slug
            String password,      // optional
            Long expiresInSec     // optional; if null => never expires
    ) {}

    public record UpdateRequest(
            Boolean revoke,
            Long extendBySec      // optional positive to extend
    ) {}

    public record ShareResponse(
            Long id, String code, String slug,
            boolean passwordProtected,
            Instant expiresAt
    ) {
        public ShareResponse(ShareLink s) {
            this(s.getId(), s.getCode(), s.getSlug(), s.getPasswordHash()!=null, s.getExpiresAt());
        }
    }

    // Public resolve (password challenge)
    public record ResolveRequest(String password) {}
    public record ResolveResponse(String downloadUrl) {}
}
