package com.acme.clouddrive.shortener;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface ShortUrlHitRepository extends JpaRepository<ShortUrlHit, Long> {
  long countByShortIdAndHitAtBetween(Long shortId, Instant from, Instant to);
}
