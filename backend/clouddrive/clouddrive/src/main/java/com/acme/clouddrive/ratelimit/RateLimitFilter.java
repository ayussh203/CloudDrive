package com.acme.clouddrive.ratelimit;

import com.acme.clouddrive.errors.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Very simple in-memory rate limiter.
 *
 * v1:
 *  - Limits /api/auth/login and /api/auth/register per IP per minute
 *  - Limits public share/redirect endpoints (/u/* and /s/*) per IP per minute
 *
 * This is NOT distributed and is only meant for single-instance / dev usage.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static class Window {
        long windowStartMs;
        int count;

        Window(long windowStartMs, int count) {
            this.windowStartMs = windowStartMs;
            this.count = count;
        }
    }

    // Simple in-memory store: key -> window
    private final Map<String, Window> buckets = new ConcurrentHashMap<>();

    // 1 minute window
    private static final long WINDOW_MS = 60_000L;

    // Limits per minute
    private static final int AUTH_LIMIT = 10;      // login / register per IP
    private static final int PUBLIC_LIMIT = 60;    // public endpoints per IP

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Only filter:
        //  - /api/auth/login
        //  - /api/auth/register
        //  - /u/*
        //  - /s/*
        if (path.equals("/api/auth/login") || path.equals("/api/auth/register")) {
            return false;
        }
        if (path.startsWith("/u/") || path.startsWith("/s/")) {
            return false;
        }
        return true;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        String ip = request.getRemoteAddr();
        long now = System.currentTimeMillis();

        int limit = isAuthPath(path) ? AUTH_LIMIT : PUBLIC_LIMIT;

        String key = buildKey(path, ip);
        Window w = buckets.compute(key, (k, existing) -> {
            if (existing == null) {
                return new Window(now, 1);
            }

            long elapsed = now - existing.windowStartMs;
            if (elapsed > WINDOW_MS) {
                // New window
                return new Window(now, 1);
            } else {
                // Same window, increment
                existing.count += 1;
                return existing;
            }
        });

        if (w.count > limit) {
            // Too many requests in this window
            HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;

            ErrorResponse body = new ErrorResponse(
                    status.value(),
                    status.getReasonPhrase(),
                    "rate_limit_exceeded",
                    request.getRequestURI()
            );

            response.setStatus(status.value());
            response.setContentType("application/json");

            String json = String.format(
                    "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                    body.getTimestamp(),
                    body.getStatus(),
                    body.getError(),
                    body.getMessage(),
                    body.getPath()
            );
            response.getWriter().write(json);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthPath(String path) {
        return "/api/auth/login".equals(path) || "/api/auth/register".equals(path);
    }

    private String buildKey(String path, String ip) {
        // Group auth endpoints together, public endpoints together
        if (isAuthPath(path)) {
            return "AUTH:" + ip;
        }
        if (path.startsWith("/u/")) {
            return "U:" + ip;
        }
        if (path.startsWith("/s/")) {
            return "S:" + ip;
        }
        return "OTHER:" + ip;
    }
}
