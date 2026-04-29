package io.zalord.identity.features.login;

import org.springframework.stereotype.Service;

import io.zalord.identity.internal.repositories.UserRepository;
import io.zalord.identity.internal.repositories.UserRepository.LoginView;
import jakarta.transaction.Transactional;

@Service
public class LoginUseCase {

    private final UserRepository userRepository;

    public LoginUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public LoginView execute(String phoneNumber) {
        
        LoginView user = userRepository.findLoginViewByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new IllegalArgumentException("User not found. Please register first."));

        return user;
    }
}