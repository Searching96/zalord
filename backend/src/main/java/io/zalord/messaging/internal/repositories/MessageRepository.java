package io.zalord.messaging.internal.repositories;

import io.zalord.messaging.internal.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
}