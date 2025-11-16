package com.acme.clouddrive.share;

import com.acme.clouddrive.files.FileObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

import static com.acme.clouddrive.share.ShareDtos.*;

@RestController
@RequestMapping("/s")
public class PublicShareController {

    private final ShareService service;
    public PublicShareController(ShareService service) { this.service = service; }

    // GET for direct browser access:
    // - if no password, 302 redirect to signed URL (5 min)
    // - if password protected, 401 with {"requiresPassword":true}
    @GetMapping("/{code}")
    public ResponseEntity<?> open(@PathVariable String code) {
        var opt = service.findActiveByCode(code);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        var s = opt.get();
        if (s.getPasswordHash() != null) {
            return ResponseEntity.status(401).body("{\"requiresPassword\":true}");
        }
        FileObject f = service.requireFileOwned(s.getFileId(), s.getOwnerId());
        String url = service.presignDownload(f.getS3Key(), Duration.ofMinutes(5));
        return ResponseEntity.status(302).header(HttpHeaders.LOCATION, url).build();
    }

    // POST to resolve with password (used by UI)
    @PostMapping("/{code}/resolve")
    public ResponseEntity<?> resolve(@PathVariable String code, @RequestBody ResolveRequest req) {
        var opt = service.findActiveByCode(code);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        var s = opt.get();
        if (!service.verifyPassword(s, req.password()))
            return ResponseEntity.status(401).body("{\"error\":\"invalid_password\"}");
        FileObject f = service.requireFileOwned(s.getFileId(), s.getOwnerId());
        String url = service.presignDownload(f.getS3Key(), Duration.ofMinutes(5));
        return ResponseEntity.ok(new ResolveResponse(url));
    }
}
