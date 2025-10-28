package com.acme.clouddrive.files;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.acme.clouddrive.files.FileDtos.*;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

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
}
