package io.zalord.identity.features.login;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zalord.identity.internal.repositories.UserRepository.LoginView;

@RestController
@RequestMapping("/users")
public class LoginEndpoint {
    
    private final LoginUseCase useCase;

    public LoginEndpoint(LoginUseCase useCase) {
        this.useCase = useCase;
    }

    public record LoginRequest(String phoneNumber) {}
    public record LoginResponse(UUID id, String displayName) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginView user = useCase.execute(request.phoneNumber());
        LoginResponse response = new LoginResponse(user.getId(), user.getDisplayName());

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}