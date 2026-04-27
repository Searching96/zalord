package io.zalord.messaging.features.getchathistory;

import io.zalord.messaging.internal.entities.MessageEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class GetChatHistoryEndpoint {

    private final GetChatHistoryUseCase useCase;

    public GetChatHistoryEndpoint(GetChatHistoryUseCase useCase) {
        this.useCase = useCase;
    }

    public record MessageHistoryResponse(UUID id, UUID senderId, String content, Instant createdAt) {}
    
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getHistory(@PathVariable UUID chatId, @RequestParam UUID userId) {

        try {
            List<MessageEntity> messages = useCase.execute(chatId, userId);

            // Map the DB entities to response DTO
            List<MessageHistoryResponse> response = messages.stream()
                .map(m -> new MessageHistoryResponse(
                    m.getId(), 
                    m.getSenderId(), 
                    m.getContent(),
                    m.getCreatedAt()
                ))
                .toList();
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}