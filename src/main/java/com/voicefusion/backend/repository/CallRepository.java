package com.voicefusion.backend.repository;

import com.voicefusion.backend.entity.Calls;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallRepository extends JpaRepository<Calls, Long> {
    List<Calls> findByCallerIdOrReceiverIdAndStatus(Long callerId, Long receiverId, String status);
}
