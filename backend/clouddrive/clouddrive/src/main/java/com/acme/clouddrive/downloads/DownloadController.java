package com.acme.clouddrive.downloads;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class DownloadController {

    private final DownloadService service;

    public DownloadController(DownloadService service) {
        this.service = service;
    }

    @GetMapping("/{id}/download")
    public Map<String, String> download(Authentication auth, @PathVariable Long id) {
        var url = service.presignGet(auth.getName(), id);
        return Map.of("url", url.toString());
    }
}
