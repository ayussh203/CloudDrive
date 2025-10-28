package com.acme.clouddrive.files;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileObjectRepository extends JpaRepository<FileObject, Long> {
    List<FileObject> findAllByOwnerIdOrderByCreatedAtDesc(Long ownerId, Pageable pageable);
    Optional<FileObject> findByIdAndOwnerId(Long id, Long ownerId);
}
