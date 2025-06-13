package com.voicefusion.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_call")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private LocalDate callDate;
    private int callCount;
}
