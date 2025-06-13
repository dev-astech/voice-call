// ======================== CONTROLLERS ========================
package com.voicefusion.backend.controller;

import com.voicefusion.backend.entity.User;
import com.voicefusion.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private AuthService authService;

    @PostMapping("/update-profile")
    public ResponseEntity<User> updateProfile(@RequestBody ProfileRequest request) {
        User user = authService.updateProfile(request.userId, request.gender,
                request.ageRange, request.primaryLanguage, request.interests);
        return ResponseEntity.ok(user);
    }

    static class ProfileRequest {
        public Long userId;
        public String gender;
        public String ageRange;
        public String primaryLanguage;
        public List<String> interests;
    }
}

