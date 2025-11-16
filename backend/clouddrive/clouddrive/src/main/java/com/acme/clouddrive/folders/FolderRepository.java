package com.acme.clouddrive.folders;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
  List<Folder> findAllByOwnerIdAndParentIdOrderByNameAsc(Long ownerId, Long parentId);
  Optional<Folder> findByIdAndOwnerId(Long id, Long ownerId);
  boolean existsByOwnerIdAndParentIdAndName(Long ownerId, Long parentId, String name);

  @Query("select f from Folder f where f.ownerId = :ownerId and f.path like concat(:path,'%') order by f.path")
  List<Folder> findSubtree(Long ownerId, String path);
}
