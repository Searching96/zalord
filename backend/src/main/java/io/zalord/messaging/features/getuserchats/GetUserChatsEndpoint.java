package io.zalord.messaging.features.getuserchats;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class GetUserChatsEndpoint {

    private final GetUserChatsUseCase useCase;

    public GetUserChatsEndpoint(GetUserChatsUseCase useCase) {
        this.useCase = useCase;
    }

    public record GetUserChatsResponse(UUID chatId, String chatName) {}

    @GetMapping("/{userId}/chats")
    public ResponseEntity<List<GetUserChatsResponse>> getUserChats(@PathVariable UUID userId) {
        
        List<GetUserChatsResponse> response = useCase.execute(userId)
            .stream()
            .map(result -> new GetUserChatsResponse(result.chatId(), result.chatName()))
            .toList();

        return ResponseEntity.ok(response);
    }
}