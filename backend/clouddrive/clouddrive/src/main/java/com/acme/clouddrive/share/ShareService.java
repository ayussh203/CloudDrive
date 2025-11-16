package com.acme.clouddrive.share;

import com.acme.clouddrive.files.FileObject;
import com.acme.clouddrive.files.FileObjectRepository;
import com.acme.clouddrive.user.User;
import com.acme.clouddrive.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShareService {

    private final ShareLinkRepository shares;
    private final FileObjectRepository files;
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final S3Presigner presigner;
    private final String bucket;

    public ShareService(ShareLinkRepository shares,
                        FileObjectRepository files,
                        UserRepository users,
                        PasswordEncoder passwordEncoder,
                        S3Presigner presigner,
                        com.acme.clouddrive.AppProps appProps) {
        this.shares = shares;
        this.files = files;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.presigner = presigner;
        this.bucket = appProps.getS3Bucket();
    }

    private Long requireUserId(String email) {
        User u = users.findByEmail(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalArgumentException("user_not_found"));
        return u.getId();
    }

    private String randomCode() {
        // 10-char base62-ish code
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    @Transactional
    public ShareLink create(String email, ShareDtos.CreateRequest req) {
        Long ownerId = requireUserId(email);

        FileObject f = files.findByIdAndOwnerId(req.fileId(), ownerId)
                .orElseThrow(() -> new IllegalArgumentException("file_not_found"));

        ShareLink s = new ShareLink();
        s.setOwnerId(ownerId);
        s.setFileId(f.getId());
        s.setCode(randomCode());

        if (req.slug() != null && !req.slug().isBlank()) {
            String slug = req.slug().trim().toLowerCase(Locale.ROOT);
            if (!slug.matches("^[a-z0-9-]{3,64}$"))
                throw new IllegalArgumentException("invalid_slug");
            if (shares.existsByOwnerIdAndSlugIgnoreCase(ownerId, slug))
                throw new IllegalStateException("slug_taken");
            s.setSlug(slug);
        }

        if (req.password() != null && !req.password().isBlank()) {
            s.setPasswordHash(passwordEncoder.encode(req.password()));
        }

        if (req.expiresInSec() != null && req.expiresInSec() > 0) {
            s.setExpiresAt(Instant.now().plusSeconds(req.expiresInSec()));
        }

        return shares.save(s);
    }

    @Transactional
    public ShareLink update(String email, Long id, ShareDtos.UpdateRequest req) {
        Long ownerId = requireUserId(email);
        ShareLink s = shares.findById(id)
                .filter(x -> x.getOwnerId().equals(ownerId))
                .orElseThrow(() -> new IllegalArgumentException("not_found"));

        if (Boolean.TRUE.equals(req.revoke())) {
            s.setRevoked(true);
        }
        if (req.extendBySec() != null && req.extendBySec() > 0) {
            Instant base = Optional.ofNullable(s.getExpiresAt()).orElse(Instant.now());
            s.setExpiresAt(base.plusSeconds(req.extendBySec()));
        }
        return shares.save(s);
    }

    public Optional<ShareLink> findActiveByCode(String code) {
        return shares.findActiveByCode(code, Instant.now());
    }

    public String presignDownload(String s3Key, Duration ttl) {
        GetObjectRequest get = GetObjectRequest.builder()
                .bucket(bucket).key(s3Key).build();
        GetObjectPresignRequest pre = GetObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .getObjectRequest(get).build();
        URL url = presigner.presignGetObject(pre).url();
        return url.toString();
    }

    public FileObject requireFileOwned(Long fileId, Long ownerId) {
        return files.findByIdAndOwnerId(fileId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("file_not_found"));
    }

    public boolean verifyPassword(ShareLink s, String raw) {
        if (s.getPasswordHash() == null) return true;
        return raw != null && passwordEncoder.matches(raw, s.getPasswordHash());
    }
}
