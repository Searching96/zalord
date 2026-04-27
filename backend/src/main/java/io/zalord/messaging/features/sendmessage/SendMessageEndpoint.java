package io.zalord.messaging.features.sendmessage;

import io.zalord.messaging.internal.entities.MessageEntity;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats")
public class SendMessageEndpoint {
    private final SendMessageUseCase sendMessageUseCase;

    public SendMessageEndpoint(SendMessageUseCase sendMessageUseCase) {
        this.sendMessageUseCase = sendMessageUseCase;
    }

    // Input DTO
    public record SendMessageRequest(UUID senderId, String content) {}
    // Output DTO
    public record SendMessageResponse(UUID messageId, UUID chatId, UUID senderId, String content) {}

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable UUID chatId,
                                         @RequestBody SendMessageRequest request) {
        try {
            MessageEntity savedMessage = sendMessageUseCase.execute(
                chatId, 
                request.senderId(), 
                request.content()
            );

            // Map entity back to a safe response DTO
            SendMessageResponse response = new SendMessageResponse(
                savedMessage.getId(), 
                savedMessage.getChatId(), 
                savedMessage.getSenderId(), 
                savedMessage.getContent()
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}