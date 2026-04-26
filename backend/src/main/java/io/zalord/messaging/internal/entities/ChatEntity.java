package io.zalord.messaging.internal.entities;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chats", schema = "messaging")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatEntity extends BaseEntity{

    @Column(nullable = false, length = 20)
    private String type; // e.g., "DM" or "GROUP"

    public ChatEntity(UUID id, String type) {
        super(id);
        this.type = type;
    }
}
