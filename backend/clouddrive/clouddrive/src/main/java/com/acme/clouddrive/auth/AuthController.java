package com.acme.clouddrive.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.acme.clouddrive.auth.AuthDtos.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        service.register(req.email, req.password);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest req) {
        String token = service.login(req.email, req.password);
        return new TokenResponse(token);
    }
}
