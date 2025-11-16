package com.acme.clouddrive.folders;

import jakarta.validation.constraints.NotBlank;

public class FolderDtos {
  public record CreateRequest(@NotBlank String name, Long parentId) {}
  public record RenameRequest(@NotBlank String name) {}
  public record MoveRequest(Long newParentId) {}
  public record FolderResponse(Long id, String name, Long parentId, String path) {}
}
