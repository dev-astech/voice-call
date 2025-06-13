package com.voicefusion.backend.controller;

import com.voicefusion.backend.entity.User;
import com.voicefusion.backend.service.AuthService;
import com.voicefusion.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            User user = authService.registerUser(
                    request.username, request.email, request.password,
                    request.gender, request.ageRange, request.primaryLanguage, request.interests);
            Map<String, String> tokens = tokenService.generateTokens(user.getId().toString());
            return ResponseEntity.ok(tokens);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = authService.loginUser(request.email, request.password);
            Map<String, String> tokens = tokenService.generateTokens(user.getId().toString());
            return ResponseEntity.ok(tokens);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/guest")
    public ResponseEntity<?> guest() {
        User user = authService.createGuestUser();
        Map<String, String> tokens = tokenService.generateTokens(user.getId().toString());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailTokenRequest req) {
        try {
            authService.verifyEmail(req.email, req.token);
            return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailRequest req) {
        try {
            authService.sendResetPasswordCode(req.email);
            return ResponseEntity.ok(Map.of("message", "Reset code sent"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetRequest req) {
        try {
            authService.resetPassword(req.token, req.newPassword);
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    static class SignupRequest {
        public String username;
        public String email;
        public String password;
        public String gender;
        public String ageRange;
        public String primaryLanguage;
        public List<String> interests;
    }

    static class LoginRequest {
        public String email;
        public String password;
    }

    static class EmailTokenRequest {
        public String email;
        public String token;
    }

    static class EmailRequest {
        public String email;
    }

    static class ResetRequest {
        public String token;
        public String newPassword;
    }
}
