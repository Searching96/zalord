package io.zalord.identity.features.registeruser;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zalord.identity.internal.entities.UserEntity;

@RestController
@RequestMapping("/api/v1/users")
public class RegisterUserEndpoint {
    final private RegisterUserUseCase registerUserUseCase;

    public RegisterUserEndpoint(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    public record RegisterRequest(String phoneNumber, String displayName) {}
    public record RegisterResponse(UUID id, String displayName) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            UserEntity user = registerUserUseCase.execute(request.phoneNumber(), request.displayName());
            return ResponseEntity.ok(new RegisterResponse(user.getId(), user.getDisplayName()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}