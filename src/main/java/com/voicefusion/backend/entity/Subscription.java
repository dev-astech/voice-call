package com.voicefusion.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String plan;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;

    @PrePersist
    public void prePersist() {
        if (startDate == null) startDate = LocalDateTime.now();
        if (status == null) status = "ACTIVE";
    }
}
