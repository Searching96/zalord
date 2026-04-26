package io.zalord.messaging.internal.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // Crucial for JPA to compare composite keys correctly
public class ChatMemberId implements Serializable {

    @Column(name = "chat_id")
    private UUID chatId;

    @Column(name = "user_id")
    private UUID userId;
}