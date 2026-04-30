package io.zalord.messaging.features.createchat;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zalord.messaging.features.MessagingUserNotFoundException;
import io.zalord.messaging.internal.entities.ChatEntity;

@RestController
@RequestMapping("/chats")
public class CreateChatEndpoint {
    
    private final CreateChatUseCase useCase;

    CreateChatEndpoint(CreateChatUseCase useCase) {
        this.useCase = useCase;
    }

    public record CreateChatRequest(List<UUID> participantIds) {}
    public record CreateChatResponse(UUID chatId, String type) {}

    @PostMapping
    public ResponseEntity<?> createChat(@RequestBody CreateChatRequest request) {
        ChatEntity chat = useCase.execute(request.participantIds());
        return ResponseEntity.ok(new CreateChatResponse(chat.getId(), chat.getType()));
    }

    @ExceptionHandler(MessagingUserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(MessagingUserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
