package com.acme.clouddrive.shortener;

import java.time.Instant;

public class ShortDtos {
  public record CreateRequest(String targetUrl, String slug, Long expiresInSec) {}
  public record UpdateRequest(Boolean active, Long extendBySec) {}
  public record ShortResponse(Long id, String code, String slug, String targetUrl,
                              boolean active, Instant expiresAt, Instant createdAt) {
    public ShortResponse(ShortUrl s){
      this(s.getId(), s.getCode(), s.getSlug(), s.getTargetUrl(),
           s.isActive(), s.getExpiresAt(), s.getCreatedAt());
    }
  }
  public record StatsResponse(long last24h, long last7d, long total){ }
}
