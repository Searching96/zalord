package io.zalord.messaging.features.finddmchat;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.zalord.messaging.features.finddmchat.FindDmChatUseCase.FindDmChatResult;

@RestController
@RequestMapping("/chats")
public class FindDmChatEndpoint {

    private final FindDmChatUseCase useCase;

    public FindDmChatEndpoint(FindDmChatUseCase useCase) {
        this.useCase = useCase;
    }

    public record FindDmChatResponse(UUID chatId, String type) {}

    @GetMapping("/dm")
    public ResponseEntity<FindDmChatResponse> findDmChat(@RequestParam UUID userA,
                                                         @RequestParam UUID userB) {
        FindDmChatResult result = useCase.execute(userA, userB);
        return ResponseEntity.ok(new FindDmChatResponse(result.chatId(), result.type()));
    }

    @ExceptionHandler(DmChatNotFoundException.class)
    public ResponseEntity<String> handleChatNotFound(DmChatNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
