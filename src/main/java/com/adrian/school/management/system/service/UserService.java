package com.adrian.school.management.system.service;

import com.adrian.school.management.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isUserExistsWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
