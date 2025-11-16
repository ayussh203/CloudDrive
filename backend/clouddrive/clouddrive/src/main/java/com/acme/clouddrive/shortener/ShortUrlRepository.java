package com.acme.clouddrive.shortener;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.*;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
  Optional<ShortUrl> findByCode(String code);
  boolean existsByCodeIgnoreCase(String code);

  boolean existsByOwnerIdAndSlugIgnoreCase(Long ownerId, String slug);

  @Query("select s from ShortUrl s where s.ownerId=:owner and s.isActive=true " +
         "and (s.expiresAt is null or s.expiresAt > :now)")
  List<ShortUrl> findActiveByOwner(@Param("owner") Long ownerId, @Param("now") Instant now);
}
