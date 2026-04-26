package io.zalord.identity.features.registeruser;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.zalord.identity.internal.entities.UserEntity;
import io.zalord.identity.internal.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class RegisterUserUseCase {
    private final UserRepository userRepository;

    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity execute(String phoneNumber, String displayName) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already exists.");
        }
        UserEntity user = new UserEntity(UUID.randomUUID(), phoneNumber, displayName);
        return userRepository.save(user);
    }
}