package io.zalord.identity.internal;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zalord.identity.IdentityApi;
import io.zalord.identity.internal.entities.UserEntity;
import io.zalord.identity.internal.repositories.UserRepository;

@Service
class IdentityApiImpl implements IdentityApi {
    private final UserRepository userRepository;

    IdentityApiImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(UUID userId) {
        return userRepository.existsById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, String> getDisplayNames(Set<UUID> userIds) {
        return userRepository.findAllById(userIds)
            .stream()
            .collect(Collectors.toMap(
                UserEntity::getId,
                UserEntity::getDisplayName
            ));    
    }
}