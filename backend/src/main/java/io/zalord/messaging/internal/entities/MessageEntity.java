package io.zalord.messaging.internal.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages", schema = "messaging")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageEntity extends BaseEntity {

    @Column(name = "chat_id", nullable = false)
    private UUID chatId;

    @Column(name = "sender_id", nullable = false)
    private UUID  senderId; // Soft reference to Identity context

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    public MessageEntity(UUID id, UUID chatId, UUID senderId, String content) {
        super(id);
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
    }
}