package com.voicefusion.backend.controller;

import com.voicefusion.backend.entity.Matches;
import com.voicefusion.backend.service.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/match")
public class MatchmakingController {

    @Autowired
    private MatchmakingService matchmakingService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> findMatch(@PathVariable Long userId) {
        Optional<Matches> match = matchmakingService.findMatch(userId);
        if (match.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "callId", match.get().getCallId(),
                    "user1Id", match.get().getUser1Id(),
                    "user2Id", match.get().getUser2Id()
            ));
        }
        return ResponseEntity.ok(Map.of("message", "No match found"));
    }

    @PutMapping("/{userId}/availability")
    public ResponseEntity<?> updateAvailability(@PathVariable Long userId, @RequestBody AvailabilityRequest req) {
        matchmakingService.updateAvailability(userId, req.isAvailable);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/clear")
    public ResponseEntity<?> clearMatch(@PathVariable Long userId) {
        matchmakingService.clearMatch(userId);
        return ResponseEntity.ok().build();
    }

    static class AvailabilityRequest {
        public boolean isAvailable;
    }
}
