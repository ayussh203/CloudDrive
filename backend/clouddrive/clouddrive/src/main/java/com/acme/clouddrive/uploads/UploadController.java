package com.acme.clouddrive.uploads;

import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.acme.clouddrive.uploads.UploadDtos.PresignRequest;
import static com.acme.clouddrive.uploads.UploadDtos.PresignResponse;

@RestController
@RequestMapping("/api/files")
public class UploadController {

    private final UploadService service;

    public UploadController(UploadService service) {
        this.service = service;
    }

    @PostMapping("/presign-upload")
    public PresignResponse presign(Authentication auth, @Validated @RequestBody PresignRequest req) {
        return service.presignPut(auth.getName(), req);
    }
}
