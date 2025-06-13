package com.voicefusion.backend.repository;

import com.voicefusion.backend.entity.UserCall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UserCallRepository extends JpaRepository<UserCall, Long> {
    Optional<UserCall> findByUserIdAndCallDate(Long userId, LocalDate callDate);
}
