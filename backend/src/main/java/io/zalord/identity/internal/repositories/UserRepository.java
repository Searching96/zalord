package io.zalord.identity.internal.repositories;

import io.zalord.identity.internal.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    interface LoginView {
        UUID getId();
        String getDisplayName();
        // String getPasswordHash(); // For future
    }

    interface SearchUserView {
        UUID getId();
        String getDisplayName();
    }

    boolean existsByPhoneNumber(String phoneNumber);

    // JPQL will derive the query on its own so we don't have to write explicit @Query
    Optional<LoginView> findLoginViewByPhoneNumber(String phoneNumber);

    Optional<SearchUserView> findSearchUserViewByPhoneNumber(String phoneNumber);
}