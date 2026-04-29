package io.zalord.identity.features.login;

import org.springframework.stereotype.Service;

import io.zalord.identity.internal.entities.UserEntity;
import io.zalord.identity.internal.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class LoginUseCase {

    private final UserRepository userRepository;

    public LoginUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity execute(String phoneNumber) {
        
        UserEntity user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new IllegalArgumentException("User not found. Please register first."));

        return user;
    }
}