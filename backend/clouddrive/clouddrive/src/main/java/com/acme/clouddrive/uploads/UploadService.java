package com.acme.clouddrive.uploads;

import com.acme.clouddrive.files.FileObject;
import com.acme.clouddrive.files.FileObjectRepository;
import com.acme.clouddrive.user.User;
import com.acme.clouddrive.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import static com.acme.clouddrive.uploads.UploadDtos.PresignRequest;
import static com.acme.clouddrive.uploads.UploadDtos.PresignResponse;

@Service
public class UploadService {

    private final S3Presigner presigner;
    private final String bucket;
    private final UserRepository users;
    private final FileObjectRepository files;

    public UploadService(S3Presigner presigner,
                         @Value("${app.s3.bucket}") String bucket,
                         UserRepository users,
                         FileObjectRepository files) {
        this.presigner = presigner;
        this.bucket = bucket;
        this.users = users;
        this.files = files;
    }

    private Long userIdByEmail(String email) {
        User u = users.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("user_not_found"));
        return u.getId();
    }

    @Transactional
    public PresignResponse presignPut(String requesterEmail, PresignRequest req) {
        Long ownerId = userIdByEmail(requesterEmail);
        String key = "uploads/%d/%s/%s".formatted(ownerId, UUID.randomUUID(), req.originalName);

        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(req.mimeType) // MUST be sent on upload
                .build();

        PutObjectPresignRequest presignReq = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(put)
                .build();

        PresignedPutObjectRequest presigned = presigner.presignPutObject(presignReq);
        URL url = presigned.url();

        // Create metadata row now (so list shows the item immediately)
        FileObject f = new FileObject();
        f.setOwnerId(ownerId);
        f.setS3Key(key);
        f.setOriginalName(req.originalName);
        f.setMimeType(req.mimeType);
        f.setSizeBytes(req.sizeBytes);
        f.setChecksumSha256(req.checksumSha256);
        files.save(f);

        return new PresignResponse(url.toString(), key, req.mimeType);
    }
}
