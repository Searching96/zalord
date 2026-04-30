package io.zalord.identity.features.register;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.zalord.identity.internal.entities.UserEntity;
import io.zalord.identity.internal.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class RegisterUseCase {
    private final UserRepository userRepository;

    public RegisterUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity execute(String phoneNumber, String displayName) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PhoneNumberAlreadyExistsException(phoneNumber);
        }
        UserEntity user = new UserEntity(UUID.randomUUID(), phoneNumber, displayName);
        return userRepository.save(user);
    }
}