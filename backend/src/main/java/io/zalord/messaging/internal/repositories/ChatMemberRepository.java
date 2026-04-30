package io.zalord.messaging.internal.repositories;

import io.zalord.messaging.internal.entities.ChatMemberEntity;
import io.zalord.messaging.internal.entities.ChatMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity, ChatMemberId> {
    
    interface ChatPartnerView {
        UUID getChatId();
        UUID getPartnerId();
    }

    // Spring Data automatically implements this to check if a user is in a specific room
    boolean existsById_ChatIdAndId_UserId(UUID chatId, UUID userId);

    // Fetch all chats for a user, alongside the UUID of the other member
    // Reduced the amount of data retrieved from the database by fetching only the neces-
    // sary fields via JPQL projection.
    @Query("""
        SELECT c.id.chatId as chatId, c.id.userId as partnerId
        FROM ChatMemberEntity c
        WHERE c.id.chatId IN (SELECT cm.id.chatId FROM ChatMemberEntity cm WHERE cm.id.userId = :userId)
        AND c.id.userId != :userId
    """)
    List<ChatPartnerView> findChatPartners(@Param("userId") UUID userId);

    @Query("""
        SELECT cm.id.chatId
        FROM ChatMemberEntity cm
        JOIN cm.chat c
        WHERE c.type = :type
            AND cm.id.chatId IN (
                SELECT cm1.id.chatId FROM ChatMemberEntity cm1 WHERE cm1.id.userId = :userA
            )
            AND cm.id.chatId IN (
                SELECT cm2.id.chatId FROM ChatMemberEntity cm2 WHERE cm2.id.userId = :userB
            )
        GROUP BY cm.id.chatId
        HAVING COUNT(cm.id.userId) = 2
    """)
    List<UUID> findChatIdsByTypeAndParticipants(@Param("type") String type,
                                                @Param("userA") UUID userA,
                                                @Param("userB") UUID userB);
}