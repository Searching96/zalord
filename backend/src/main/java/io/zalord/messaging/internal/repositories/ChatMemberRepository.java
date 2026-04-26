package io.zalord.messaging.internal.repositories;

import io.zalord.messaging.internal.entities.ChatMemberEntity;
import io.zalord.messaging.internal.entities.ChatMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity, ChatMemberId> {
    
    // Spring Data automatically implements this to check if a user is in a specific room
    boolean existsById_ChatIdAndId_UserId(UUID chatId, UUID userId);
}