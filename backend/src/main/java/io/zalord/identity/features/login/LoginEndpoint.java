package io.zalord.identity.features.login;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zalord.identity.internal.entities.UserEntity;

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
        try {
            UserEntity user = useCase.execute(request.phoneNumber());
            LoginResponse response = new LoginResponse(user.getId(), user.getDisplayName());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}