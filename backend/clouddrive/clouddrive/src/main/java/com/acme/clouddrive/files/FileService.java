package com.acme.clouddrive.files;

import com.acme.clouddrive.files.FileDtos.CreateRequest;
import com.acme.clouddrive.files.FileDtos.UpdateRequest;
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

        private final FileVersionRepository fileVersions;


    // --- S3 wiring ---
    private final S3Client s3;
    private final String bucket;

    public FileService(FileObjectRepository files,
                       UserRepository users,
                       S3Client s3,
                       @Value("${app.s3.bucket}") String bucket,
                     FileVersionRepository fileVersions) {
        this.files = files;
        this.users = users;
        this.s3 = s3;
        this.bucket = bucket;
          this.fileVersions = fileVersions;
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
         f.setVersion(1);
        FileObject saved = files.save(f);
        fileVersions.save(FileVersion.fromFile(saved, 1));
        return saved;
    }

        @Transactional
    public FileObject uploadNewVersion(String email, Long fileId, MultipartFile file) throws IOException {
        Long ownerId = requireUserIdByEmail(email);

        FileObject existing = files.findByIdAndOwnerId(fileId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("file_not_found"));

        String originalName = Objects.requireNonNullElse(file.getOriginalFilename(), existing.getOriginalName());

        // Reuse same folder prefix as existing file
        String existingKey = existing.getS3Key();
        int idx = existingKey.lastIndexOf('/');
        String prefix = (idx > 0) ? existingKey.substring(0, idx) : "uploads/" + ownerId;

        String key = String.format("%s/%s/%s", prefix, UUID.randomUUID(), originalName);

        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3.putObject(put, RequestBody.fromBytes(file.getBytes()));

        // Determine next version number
        int currentCount = fileVersions.countByFileId(existing.getId());
        int nextVersion = Math.max(currentCount + 1, (existing.getVersion() == null ? 1 : existing.getVersion() + 1));

        // Update FileObject to point to latest version
        existing.setS3Key(key);
        existing.setOriginalName(originalName);
        existing.setMimeType(file.getContentType());
        existing.setSizeBytes(file.getSize());
        existing.setVersion(nextVersion);

        FileObject saved = files.save(existing);

        // Record a new FileVersion row
        fileVersions.save(FileVersion.fromFile(saved, nextVersion));

        return saved;
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
        f.setVersion(1);

        FileObject saved = files.save(f);
        fileVersions.save(FileVersion.fromFile(saved, 1));
        return saved;
    }
    public List<FileVersion> listVersions(String email, Long fileId) {
        Long ownerId = requireUserIdByEmail(email);

        FileObject existing = files.findByIdAndOwnerId(fileId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("file_not_found"));

        return fileVersions.findByFileIdOrderByVersionDesc(existing.getId());
    }

        @Transactional
    public FileObject restoreVersion(String email, Long fileId, int version) {
        Long ownerId = requireUserIdByEmail(email);

        FileObject existing = files.findByIdAndOwnerId(fileId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("file_not_found"));

        FileVersion v = fileVersions.findByFileIdAndVersion(existing.getId(), version)
                .orElseThrow(() -> new IllegalArgumentException("file_version_not_found"));

        // Point FileObject back to this version's data
        existing.setS3Key(v.getS3Key());
        existing.setMimeType(v.getMimeType());
        existing.setSizeBytes(v.getSizeBytes());
        existing.setChecksumSha256(v.getChecksumSha256());
        existing.setVersion(v.getVersion());

        return files.save(existing);
    }

}
