package io.zalord.messaging.internal.repositories;

import io.zalord.messaging.internal.entities.ChatMemberEntity;
import io.zalord.messaging.internal.entities.ChatMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity, ChatMemberId> {
    
    // Spring Data automatically implements this to check if a user is in a specific room
    boolean existsById_ChatIdAndId_UserId(UUID chatId, UUID userId);

    // Reducing the amount of data retrieved from the database by fetching only the neces-
    // sary fields via JPQL projection.
    @Query("SELECT c.id.chatId from ChatMemberEntity c WHERE c.id.userId = :userid")
    List<UUID> findChatIdsByUserId(@Param("userId") UUID userId);
}