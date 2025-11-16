package com.acme.clouddrive.share;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {
    Optional<ShareLink> findByCode(String code);
    boolean existsByOwnerIdAndSlugIgnoreCase(Long ownerId, String slug);

    default Optional<ShareLink> findActiveByCode(String code, Instant now) {
        return findByCode(code).filter(sl ->
                !sl.isRevoked() && (sl.getExpiresAt()==null || sl.getExpiresAt().isAfter(now)));
    }
}
