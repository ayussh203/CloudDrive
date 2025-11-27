package com.acme.clouddrive.files;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileVersionRepository extends JpaRepository<FileVersion, Long> {

    List<FileVersion> findByFileIdOrderByVersionDesc(Long fileId);

    Optional<FileVersion> findByFileIdAndVersion(Long fileId, int version);

    int countByFileId(Long fileId);
}
