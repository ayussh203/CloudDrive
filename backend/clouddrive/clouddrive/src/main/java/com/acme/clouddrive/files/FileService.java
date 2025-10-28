package com.acme.clouddrive.files;

import com.acme.clouddrive.user.User;
import com.acme.clouddrive.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.acme.clouddrive.files.FileDtos.CreateRequest;
import static com.acme.clouddrive.files.FileDtos.UpdateRequest;

@Service
public class FileService {

    private final FileObjectRepository files;
    private final UserRepository users;

    public FileService(FileObjectRepository files, UserRepository users) {
        this.files = files;
        this.users = users;
    }

    private Long requireUserIdByEmail(String email) {
        User u = users.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("user_not_found"));
        return u.getId();
    }

    @Transactional
    public FileObject create(String email, CreateRequest req) {
        Long ownerId = requireUserIdByEmail(email);
        FileObject f = new FileObject();
        f.setOwnerId(ownerId);
        // placeholder s3 key — will be replaced by presign flow in Story 2.3
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
}
