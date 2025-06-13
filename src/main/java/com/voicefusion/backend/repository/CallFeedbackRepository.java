package com.voicefusion.backend.repository;

import com.voicefusion.backend.entity.CallFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallFeedbackRepository extends JpaRepository<CallFeedback, Long> {
}
