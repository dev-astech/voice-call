package com.voicefusion.backend.service;

import com.voicefusion.backend.entity.UserCall;
import com.voicefusion.backend.repository.UserCallRepository;
import com.voicefusion.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CallService {

    @Autowired
    private UserCallRepository userCallRepository;
    @Autowired
    private UserRepository userRepository;

    public int getDailyCallCount(Long userId) {
        return userCallRepository.findByUserIdAndCallDate(userId, LocalDate.now())
                .map(UserCall::getCallCount).orElse(0);
    }

    public void incrementCallCount(Long userId) {
        UserCall userCall = userCallRepository.findByUserIdAndCallDate(userId, LocalDate.now())
                .orElse(new UserCall(null, userRepository.findById(userId).orElseThrow(), LocalDate.now(), 0));
        userCall.setCallCount(userCall.getCallCount() + 1);
        userCallRepository.save(userCall);
    }
}
