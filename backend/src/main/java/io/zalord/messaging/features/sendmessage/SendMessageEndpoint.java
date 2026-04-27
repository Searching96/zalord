package io.zalord.messaging.features.sendmessage;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        try {
            // The Use Case already maps the entity and returns the DTO
            SendMessageResponse response = useCase.execute(
                chatId, 
                request.senderId(), 
                request.content()
            );

            // Just pass it directly to the client
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}