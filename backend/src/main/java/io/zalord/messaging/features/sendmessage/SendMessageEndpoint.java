package io.zalord.messaging.features.sendmessage;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zalord.messaging.features.ChatAccessDeniedException;
import io.zalord.messaging.features.MessagingUserNotFoundException;
@RestController
@RequestMapping("/chats")
public class SendMessageEndpoint {
    private final SendMessageUseCase useCase;

    public SendMessageEndpoint(SendMessageUseCase useCase) {
        this.useCase = useCase;
    }

    // Input DTO
    public record SendMessageRequest(UUID senderId, String content) {}
    // Output DTO
    public record SendMessageResponse(UUID id, UUID chatId, UUID senderId, String content, Instant createdAt) {}

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable UUID chatId,
                                         @RequestBody SendMessageRequest request) {
        // The Use Case already maps the entity and returns the DTO
        SendMessageResponse response = useCase.execute(
            chatId, 
            request.senderId(), 
            request.content()
        );

        // Just pass it directly to the client
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(MessagingUserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(MessagingUserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ChatAccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(ChatAccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}