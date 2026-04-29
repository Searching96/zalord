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

    @Query(
        "SELECT m.id AS id, m.senderId AS senderId, m.content AS content, m.createdAt AS createdAt " +
        "FROM MessageEntity m WHERE m.chatId = :chatId ORDER BY m.createdAt ASC"
    )
    List<MessageHistoryView> findHistoryByChatId(@Param("chatId") UUID chatId);
}