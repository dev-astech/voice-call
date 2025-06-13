// ======================== ENTITIES ========================
package com.voicefusion.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;
    private String gender;
    private String ageRange;
    private String primaryLanguage;

    private boolean isGuest;
    private boolean isEmailVerified;

    private String resetToken;
    private LocalDateTime tokenExpiration;

    private boolean isAvailable;
    private LocalDateTime lastActive;

    @ElementCollection
    private List<String> interests;
}


