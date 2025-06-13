package com.voicefusion.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calls")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Calls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User caller;

    @ManyToOne
    private User receiver;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String language;
    private String status;

    @PrePersist
    public void prePersist() {
        if (startTime == null) startTime = LocalDateTime.now();
        if (status == null) status = "INITIATED";
    }
}
