package com.voicefusion.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "call_feedback")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Calls call;

    @ManyToOne
    private User user;

    private int rating;
    private String comments;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}