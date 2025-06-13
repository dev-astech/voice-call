package com.voicefusion.backend.service;

import com.voicefusion.backend.entity.User;
import com.voicefusion.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String email, String password, String gender,
                             String ageRange, String primaryLanguage, List<String> interests) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .gender(gender)
                .ageRange(ageRange)
                .primaryLanguage(primaryLanguage)
                .interests(interests)
                .isGuest(false)
                .isEmailVerified(false)
                .resetToken(String.format("%06d", new Random().nextInt(1000000)))
                .tokenExpiration(LocalDateTime.now().plusMinutes(15))
                .build();

        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified");
        }
        return user;
    }

    public User createGuestUser() {
        User guest = User.builder()
                .username("Guest_" + UUID.randomUUID().toString().substring(0, 8))
                .isGuest(true)
                .isEmailVerified(true)
                .isAvailable(false)
                .build();
        return userRepository.save(guest);
    }

    public void verifyEmail(String email, String token) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!token.equals(user.getResetToken()) || user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired verification token");
        }

        user.setEmailVerified(true);
        user.setResetToken(null);
        user.setTokenExpiration(null);
        userRepository.save(user);
    }

    public void sendResetPasswordCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setResetToken(String.format("%06d", new Random().nextInt(1000000)));
        user.setTokenExpiration(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getResetToken()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset code expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiration(null);
        userRepository.save(user);
    }

    public User updateProfile(Long userId, String gender, String ageRange, String primaryLanguage, List<String> interests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setGender(gender);
        user.setAgeRange(ageRange);
        user.setPrimaryLanguage(primaryLanguage);
        user.setInterests(interests);

        return userRepository.save(user);
    }
}
