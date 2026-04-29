package io.zalord.identity.internal.repositories;

import io.zalord.identity.internal.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    interface LoginView {
        UUID getId();
        String getDisplayName();
    }

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u.id AS id, u.displayName AS displayName FROM UserEntity u WHERE u.phoneNumber = :phoneNumber")
    Optional<LoginView> findLoginViewByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}