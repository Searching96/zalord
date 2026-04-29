package io.zalord.messaging.internal.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_members", schema = "messaging")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMemberEntity {
    
    // Composite ID contains purely basic types (UUIDs)
    @EmbeddedId
    private ChatMemberId id;

    @Column(name = "joined_at", insertable = false, updatable = false)
    private Instant joinedAt;

    // Read-only relationship mapped to allow object graph navigation without separate re-
    // pository calls
    // Mapped outside the composite key to preserve Lazy Loading and prevent N+1 queries
    // User relationship is not mapped because it is in another context
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private ChatEntity chat;

    public ChatMemberEntity(UUID chatId, UUID userId) {
        this.id = new ChatMemberId(chatId, userId);
    }
}