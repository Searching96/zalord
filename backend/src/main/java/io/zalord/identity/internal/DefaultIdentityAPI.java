package io.zalord.identity.internal;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.zalord.identity.IdentityAPI;
import io.zalord.identity.internal.repositories.UserRepository;

@Service
class DefaultIdentityAPI implements IdentityAPI {
    private final UserRepository userRepository;

    DefaultIdentityAPI(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean userExists(UUID userId) {
        return userRepository.existsById(userId);
    }
}