// src/main/java/com/acme/clouddrive/web/SecureController.java
package com.acme.clouddrive.web;

import com.acme.clouddrive.user.User;
import com.acme.clouddrive.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

record MeDto(Long id, String email) {}

@RestController
@RequestMapping("/api/secure")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SecureController {

    private final UserRepository users;

    public SecureController(UserRepository users) {
        this.users = users;
    }

    @GetMapping("/me")
    public MeDto me(Authentication auth) {
        User u = users.findByEmail(auth.getName())
            .orElseThrow(() -> new IllegalArgumentException("user_not_found"));
        return new MeDto(u.getId(), u.getEmail());
    }
}
