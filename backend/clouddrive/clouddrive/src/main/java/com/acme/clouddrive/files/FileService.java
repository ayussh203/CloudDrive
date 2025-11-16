package com.acme.clouddrive.files;

import com.acme.clouddrive.user.User;
import com.acme.clouddrive.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.acme.clouddrive.files.FileDtos.CreateRequest;
import static com.acme.clouddrive.files.FileDtos.UpdateRequest;

@Service
public class FileService {

    private final FileObjectRepository files;
    private final UserRepository users;

    // --- S3 wiring ---
    private final S3Client s3;
    private final String bucket;

    public FileService(FileObjectRepository files,
                       UserRepository users,
                       S3Client s3,
                       @Value("${app.s3.bucket}") String bucket) {
        this.files = files;
        this.users = users;
        this.s3 = s3;
        this.bucket = bucket;
    }

    private Long requireUserIdByEmail(String email) {
        User u = users.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("user_not_found"));
        return u.getId();
    }

    // ----- Existing JSON-create flow (kept as-is) -----
    @Transactional
    public FileObject create(String email, CreateRequest req) {
        Long ownerId = requireUserIdByEmail(email);
        FileObject f = new FileObject();
        f.setOwnerId(ownerId);
        // placeholder s3 key (kept for your presign flow)
        String key = "uploads/" + ownerId + "/" + UUID.randomUUID() + "/" + req.originalName;
        f.setS3Key(key);
        f.setOriginalName(req.originalName);
        f.setMimeType(req.mimeType);
        f.setSizeBytes(req.sizeBytes);
        f.setChecksumSha256(req.checksumSha256);
        return files.save(f);
    }

    public List<FileObject> list(String email, int page, int size) {
        Long ownerId = requireUserIdByEmail(email);
        return files.findAllByOwnerIdOrderByCreatedAtDesc(ownerId, PageRequest.of(page, size));
    }

    public FileObject getOne(String email, Long id) {
        Long ownerId = requireUserIdByEmail(email);
        return files.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("not_found"));
    }

    @Transactional
    public FileObject updateName(String email, Long id, UpdateRequest req) {
        FileObject f = getOne(email, id);
        f.setOriginalName(req.originalName);
        return files.save(f);
    }

    @Transactional
    public void delete(String email, Long id) {
        FileObject f = getOne(email, id);
        files.delete(f);
    }

    // ----- NEW: Multipart upload (S3 + DB save) -----
    @Transactional
    public FileObject upload(String email, MultipartFile file, String folder) throws IOException {
        Long ownerId = requireUserIdByEmail(email);

        String safeFolder = (folder == null || folder.isBlank()) ? "uploads" : folder.trim();
        String originalName = Objects.requireNonNullElse(file.getOriginalFilename(), "file.bin");

        // S3 key: <folder>/<ownerId>/<uuid>/<originalName>
        String key = String.format("%s/%d/%s/%s",
                safeFolder, ownerId, UUID.randomUUID(), originalName);

        // Put to S3
        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3.putObject(put, RequestBody.fromBytes(file.getBytes()));

        // Save FileObject row
        FileObject f = new FileObject();
        f.setOwnerId(ownerId);
        f.setS3Key(key);
        f.setOriginalName(originalName);
        f.setMimeType(file.getContentType());
        f.setSizeBytes(file.getSize());
        // checksumSha256 left null for now (optional)
        return files.save(f);
    }
}
