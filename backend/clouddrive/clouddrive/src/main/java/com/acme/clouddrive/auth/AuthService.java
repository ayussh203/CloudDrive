package com.acme.clouddrive.auth;

import com.acme.clouddrive.user.User;
import com.acme.clouddrive.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtUtil jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Transactional
    public void register(String email, String rawPassword) {
        if (users.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
        User u = new User();
        u.setEmail(email.toLowerCase());
        u.setPasswordHash(encoder.encode(rawPassword));
        users.save(u);
    }

    public String login(String email, String rawPassword) {
        User u = users.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!encoder.matches(rawPassword, u.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwt.generateToken(u.getEmail());
    }
}
