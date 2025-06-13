// ======================== REPOSITORIES ========================
package com.voicefusion.backend.repository;

import com.voicefusion.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.isAvailable = true AND u.id != :userId ORDER BY RAND()")
    Optional<User> findRandomAvailableUser(Long userId);
}

