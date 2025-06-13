// ======================== SERVICE LAYER ========================
package com.voicefusion.backend.service;

import com.voicefusion.backend.entity.*;
import com.voicefusion.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MatchmakingService {

    @Autowired private UserRepository userRepository;
    @Autowired private MatchRepository matchRepository;

    public Optional<Matches> findMatch(Long userId) {
        Optional<Matches> existingMatch = matchRepository.findByUser1IdOrUser2Id(userId, userId);
        if (existingMatch.isPresent()) return existingMatch;

        Optional<User> currentUser = userRepository.findById(userId);
        if (currentUser.isEmpty() || !currentUser.get().isAvailable()) return Optional.empty();

        Optional<User> matchUser = userRepository.findRandomAvailableUser(userId);
        if (matchUser.isPresent()) {
            User userA = currentUser.get();
            User userB = matchUser.get();

            userA.setAvailable(false);
            userB.setAvailable(false);
            userRepository.saveAll(List.of(userA, userB));

            String callId = "call_" + Math.min(userA.getId(), userB.getId()) + "_" + Math.max(userA.getId(), userB.getId()) + "_" + System.currentTimeMillis();

            Matches matches = Matches.builder()
                    .user1Id(Math.min(userA.getId(), userB.getId()))
                    .user2Id(Math.max(userA.getId(), userB.getId()))
                    .callId(callId)
                    .matchedAt(LocalDateTime.now())
                    .build();

            return Optional.of(matchRepository.save(matches));
        }

        return Optional.empty();
    }

    public void updateAvailability(Long userId, boolean isAvailable) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setAvailable(isAvailable);
            user.setLastActive(LocalDateTime.now());
            userRepository.save(user);

            if (!isAvailable) {
                matchRepository.findByUser1IdOrUser2Id(userId, userId)
                        .ifPresent(matchRepository::delete);
            }
        });
    }

    public void clearMatch(Long userId) {
        matchRepository.findByUser1IdOrUser2Id(userId, userId).ifPresent(matches -> {
            userRepository.findById(matches.getUser1Id()).ifPresent(u -> {
                u.setAvailable(false);
                userRepository.save(u);
            });
            userRepository.findById(matches.getUser2Id()).ifPresent(u -> {
                u.setAvailable(false);
                userRepository.save(u);
            });
            matchRepository.delete(matches);
        });
    }
}

