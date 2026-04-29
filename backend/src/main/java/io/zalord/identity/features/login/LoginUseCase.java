package io.zalord.identity.features.login;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zalord.identity.internal.repositories.UserRepository;
import io.zalord.identity.internal.repositories.UserRepository.LoginView;

@Service
public class LoginUseCase {

    private final UserRepository userRepository;

    public LoginUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public LoginView execute(String phoneNumber) {
        
        LoginView user = userRepository.findLoginViewByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new IllegalArgumentException("User not found. Please register first."));

        return user;
    }
}