package com.acme.clouddrive.files;

import com.acme.clouddrive.files.FileDtos.CreateRequest;
import com.acme.clouddrive.files.FileDtos.FileResponse;
import com.acme.clouddrive.files.FileDtos.UpdateRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.acme.clouddrive.folders.FolderService;  
import com.acme.clouddrive.files.FileDtos.FileVersionResponse;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService service;
      private final FolderService folderService; 

    public FileController(FileService service,       // <-- add ctor arg
                          FolderService folderService) {
        this.service = service;
        this.folderService = folderService;         // <-- add
    }
    // ---- JSON metadata endpoints you already had ----

    @PostMapping
    public FileResponse create(Authentication auth, @Validated @RequestBody CreateRequest req) {
        return new FileResponse(service.create(auth.getName(), req));
    }

    @GetMapping
    public List<FileResponse> list(Authentication auth,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size) {
        return service.list(auth.getName(), page, size)
                .stream().map(FileResponse::new).toList();
    }

    @GetMapping("/{id}")
    public FileResponse get(Authentication auth, @PathVariable Long id) {
        return new FileResponse(service.getOne(auth.getName(), id));
    }

    @PatchMapping("/{id}")
    public FileResponse rename(Authentication auth, @PathVariable Long id,
                               @Validated @RequestBody UpdateRequest req) {
        return new FileResponse(service.updateName(auth.getName(), id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(Authentication auth, @PathVariable Long id) {
        service.delete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }



    // ---- NEW: multipart upload -> delegates to service ----
    // POST /api/files/upload  (multipart/form-data)
    @PostMapping(
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public FileResponse upload(Authentication auth,
                               @RequestPart("file") MultipartFile file,
                               @RequestParam(value = "folder", required = false, defaultValue = "uploads") String folder)
            throws IOException {
       
       
                return new FileResponse(service.upload(auth.getName(), file, folder));
    }


    @PostMapping(
            path = "/{id}/versions",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public FileResponse uploadNewVersion(
            Authentication auth,
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        return new FileResponse(service.uploadNewVersion(auth.getName(), id, file));
    }


     @GetMapping("/{id}/versions")
    public List<FileVersionResponse> listVersions(
            Authentication auth,
            @PathVariable Long id
    ) {
        return service.listVersions(auth.getName(), id).stream()
                .map(FileVersionResponse::new)
                .toList();
    }

        @PostMapping("/{id}/versions/{version}/restore")
    public FileResponse restoreVersion(
            Authentication auth,
            @PathVariable Long id,
            @PathVariable int version
    ) {
        return new FileResponse(service.restoreVersion(auth.getName(), id, version));
    }

    
    @PatchMapping("/{id}/move")
public ResponseEntity<?> moveFile(Authentication auth, @PathVariable Long id,
                                  @RequestParam(required=false) Long targetFolderId) {
    folderService.moveFile(auth.getName(), id, targetFolderId);
    return ResponseEntity.noContent().build();
}
}
