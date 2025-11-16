package com.acme.clouddrive.folders;

import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.acme.clouddrive.folders.FolderDtos.*;

@RestController
@RequestMapping("/api/folders")
public class FolderController {
  private final FolderService service;
  public FolderController(FolderService service){ this.service = service; }

  @PostMapping
  public FolderResponse create(Authentication auth, @Validated @RequestBody CreateRequest req){
    var f = service.create(auth.getName(), req.name(), req.parentId());
    return new FolderResponse(f.getId(), f.getName(), f.getParentId(), f.getPath());
  }

  @GetMapping
  public List<FolderResponse> list(Authentication auth, @RequestParam(required=false) Long parentId){
    return service.listChildren(auth.getName(), parentId).stream()
      .map(f -> new FolderResponse(f.getId(), f.getName(), f.getParentId(), f.getPath()))
      .toList();
  }

  @PatchMapping("/{id}/rename")
  public FolderResponse rename(Authentication auth, @PathVariable Long id, @Validated @RequestBody RenameRequest req){
    var f = service.rename(auth.getName(), id, req.name());
    return new FolderResponse(f.getId(), f.getName(), f.getParentId(), f.getPath());
  }

  @PatchMapping("/{id}/move")
  public FolderResponse move(Authentication auth, @PathVariable Long id, @RequestBody MoveRequest req){
    var f = service.move(auth.getName(), id, req.newParentId());
    return new FolderResponse(f.getId(), f.getName(), f.getParentId(), f.getPath());
  }

  @DeleteMapping("/{id}")
  public void delete(Authentication auth, @PathVariable Long id){
    service.delete(auth.getName(), id);
  }
}
