package io.zalord.identity.internal.repositories;

import io.zalord.identity.internal.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
}