package com.acme.clouddrive.folders;

import com.acme.clouddrive.user.User;
import com.acme.clouddrive.user.UserRepository;
import com.acme.clouddrive.files.FileObjectRepository;
import com.acme.clouddrive.files.FileObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FolderService {
  private final FolderRepository folders;
  private final UserRepository users;
  private final FileObjectRepository files;

  public FolderService(FolderRepository folders, UserRepository users, FileObjectRepository files){
    this.folders = folders; this.users = users; this.files = files;
  }

  private Long ownerIdOf(String email){
    return users.findByEmail(email.toLowerCase())
      .map(User::getId)
      .orElseThrow(() -> new IllegalArgumentException("user_not_found"));
  }

  private Folder requireOwned(Long ownerId, Long id){
    return folders.findByIdAndOwnerId(id, ownerId)
      .orElseThrow(() -> new IllegalArgumentException("folder_not_found"));
  }

  @Transactional
  public Folder create(String email, String name, Long parentId){
    Long ownerId = ownerIdOf(email);
    Long pid = parentId; // nullable = top-level

    if (folders.existsByOwnerIdAndParentIdAndName(ownerId, pid, name)) {
      throw new IllegalStateException("folder_name_taken");
    }

    Folder f = new Folder();
    f.setOwnerId(ownerId);
    f.setName(name);
    f.setParentId(pid);
    f.setPath("/tmp/");
    Folder saved = folders.save(f);

    String base = "/";
    if (pid != null) base = requireOwned(ownerId, pid).getPath();
    saved.setPath(base + saved.getId() + "/");
    return folders.save(saved);
  }

  public List<Folder> listChildren(String email, Long parentId){
    Long ownerId = ownerIdOf(email);
    return folders.findAllByOwnerIdAndParentIdOrderByNameAsc(ownerId, parentId);
  }

  @Transactional
  public Folder rename(String email, Long id, String newName){
    Long ownerId = ownerIdOf(email);
    Folder f = requireOwned(ownerId, id);
    if (folders.existsByOwnerIdAndParentIdAndName(ownerId, f.getParentId(), newName)) {
      throw new IllegalStateException("folder_name_taken");
    }
    f.setName(newName);
    return folders.save(f);
  }

  @Transactional
  public Folder move(String email, Long id, Long newParentId){
    Long ownerId = ownerIdOf(email);
    Folder f = requireOwned(ownerId, id);

    String oldPath = f.getPath();
    String base = "/";
    if (newParentId != null) {
      Folder np = requireOwned(ownerId, newParentId);
      if (np.getPath().startsWith(oldPath)) {
        throw new IllegalStateException("cannot_move_into_self");
      }
      base = np.getPath();
    }

    if (folders.existsByOwnerIdAndParentIdAndName(ownerId, newParentId, f.getName())) {
      throw new IllegalStateException("folder_name_taken");
    }

    f.setParentId(newParentId);
    f.setPath(base + f.getId() + "/");
    Folder moved = folders.save(f);

    // re-path descendants
    List<Folder> descendants = folders.findSubtree(ownerId, oldPath);
    for (Folder d : descendants) {
      if (Objects.equals(d.getId(), moved.getId())) continue;
      String suffix = d.getPath().substring(oldPath.length());
      d.setPath(moved.getPath() + suffix);
    }
    folders.saveAll(descendants);
    return moved;
  }

  @Transactional
  public void delete(String email, Long id){
    Long ownerId = ownerIdOf(email);
    Folder f = requireOwned(ownerId, id);

    // must be empty (no child folders, no files)
    if (!folders.findAllByOwnerIdAndParentIdOrderByNameAsc(ownerId, id).isEmpty())
      throw new IllegalStateException("folder_not_empty");

    if (files.existsByOwnerIdAndFolderId(ownerId, id))
      throw new IllegalStateException("folder_has_files");

    folders.delete(f);
  }

  @Transactional
  public void moveFile(String email, Long fileId, Long targetFolderId){
    Long ownerId = ownerIdOf(email);
    FileObject fo = files.findByIdAndOwnerId(fileId, ownerId)
      .orElseThrow(() -> new IllegalArgumentException("file_not_found"));
    if (targetFolderId != null) requireOwned(ownerId, targetFolderId);
    fo.setFolderId(targetFolderId);
    files.save(fo);
  }
}
