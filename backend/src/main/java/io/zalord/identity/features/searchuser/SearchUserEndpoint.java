package io.zalord.identity.features.searchuser;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.zalord.identity.features.searchuser.SearchUserUseCase.SearchUserResult;

@RestController
@RequestMapping("/users")
public class SearchUserEndpoint {

    private final SearchUserUseCase useCase;

    public SearchUserEndpoint(SearchUserUseCase useCase) {
        this.useCase = useCase;
    }

    public record SearchUserRequest(String phoneNumber) {}
    public record SearchUserResponse(UUID id, String displayName) {}

    @GetMapping("/search")
    public ResponseEntity<SearchUserResponse> searchUser(@RequestParam String phoneNumber) {
        SearchUserResult result = useCase.execute(phoneNumber);
        return ResponseEntity.ok(new SearchUserResponse(result.id(), result.displayName()));
    }

    @ExceptionHandler(SearchUserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(SearchUserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
