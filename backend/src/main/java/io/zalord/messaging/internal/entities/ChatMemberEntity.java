package io.zalord.messaging.internal.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_members", schema = "messaging")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMemberEntity {
    
    @EmbeddedId
    private ChatMemberId id;

    @Column(name = "joined_at", insertable = false, updatable = false)
    private Instant joinedAt;

    public ChatMemberEntity(UUID chatId, UUID userId) {
        this.id = new ChatMemberId(chatId, userId);
    }
}