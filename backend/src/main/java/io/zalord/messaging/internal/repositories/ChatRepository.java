package io.zalord.messaging.internal.repositories;

import io.zalord.messaging.internal.entities.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {
    
}