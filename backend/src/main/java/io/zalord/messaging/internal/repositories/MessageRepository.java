package io.zalord.messaging.internal.repositories;

import io.zalord.messaging.internal.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    interface MessageHistoryView {
        UUID getId();
        UUID getSenderId();
        String getContent();
        Instant getCreatedAt();
    }

    @Deprecated
    @Query("""
        SELECT m.id AS id, m.senderId AS senderId, m.content AS content, m.createdAt AS createdAt
        FROM MessageEntity m WHERE m.chatId = :chatId ORDER BY m.createdAt ASC
    """)
    List<MessageHistoryView> findMessageHistoryByChatId(@Param("chatId") UUID chatId);

    // This give compile-time safety because if we ever rename the createdAt or chatId fi-
    // elds in the MessageEntity, the IDE will instantly flag this function as an error.
    // With an explicit @Query, we wouldn't find out it was broken until runtime.
    // This derived query method also as efficient as the deprecated explicit query we wr-
    // ote above.
    List<MessageHistoryView> findMessageHistoryByChatIdOrderByCreatedAtAsc(UUID chatId);
}