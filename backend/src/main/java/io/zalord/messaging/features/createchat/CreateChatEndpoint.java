package io.zalord.messaging.features.createchat;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zalord.messaging.internal.entities.ChatEntity;

@RestController
@RequestMapping("/chats")
public class CreateChatEndpoint {
    
    private final CreateChatUseCase createChatUseCase;

    CreateChatEndpoint(CreateChatUseCase createChatUseCase) {
        this.createChatUseCase = createChatUseCase;
    }

    public record CreateChatRequest(List<UUID> participantIds) {}
    public record CreateChatResponse(UUID chatId, String type) {}

    @PostMapping
    public ResponseEntity<?> createChat(@RequestBody CreateChatRequest request) {
        try {
            ChatEntity chat = createChatUseCase.execute(request.participantIds());
            return ResponseEntity.ok(new CreateChatResponse(chat.getId(), chat.getType()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
