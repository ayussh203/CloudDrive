package com.acme.clouddrive.downloads;

import com.acme.clouddrive.files.FileObject;
import com.acme.clouddrive.files.FileObjectRepository;
import com.acme.clouddrive.user.User;
import com.acme.clouddrive.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class DownloadService {

    private final S3Presigner presigner;
    private final String bucket;
    private final UserRepository users;
    private final FileObjectRepository files;

    public DownloadService(S3Presigner presigner,
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

    public URL presignGet(String requesterEmail, Long fileId) {
        Long ownerId = userIdByEmail(requesterEmail);

        FileObject f = files.findById(fileId)
                .filter(x -> x.getOwnerId().equals(ownerId))
                .orElseThrow(() -> new IllegalArgumentException("not_found"));

        GetObjectRequest get = GetObjectRequest.builder()
                .bucket(bucket)
                .key(f.getS3Key())
                .responseContentType(f.getMimeType())
                .responseContentDisposition("attachment; filename=\"" + f.getOriginalName() + "\"")
                .build();

        GetObjectPresignRequest req = GetObjectPresignRequest.builder()
                .getObjectRequest(get)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        PresignedGetObjectRequest presigned = presigner.presignGetObject(req);
        return presigned.url();
    }
}
