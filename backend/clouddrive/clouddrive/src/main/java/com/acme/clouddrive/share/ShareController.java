package com.acme.clouddrive.share;

import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.acme.clouddrive.share.ShareDtos.*;

@RestController
@RequestMapping("/api/share")
public class ShareController {

    private final ShareService service;
    public ShareController(ShareService service) { this.service = service; }

    @PostMapping
    public ShareResponse create(Authentication auth, @Validated @RequestBody CreateRequest req) {
        return new ShareResponse(service.create(auth.getName(), req));
    }

    @PatchMapping("/{id}")
    public ShareResponse update(Authentication auth, @PathVariable Long id,
                                @Validated @RequestBody UpdateRequest req) {
        return new ShareResponse(service.update(auth.getName(), id, req));
    }
}
