package com.voicefusion.backend.repository;

import com.voicefusion.backend.entity.Matches;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Matches, Long> {
    Optional<Matches> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);
}
