package io.zalord.identity.features.registeruser;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zalord.identity.internal.entities.UserEntity;

@RestController
@RequestMapping("/users")
public class RegisterUserEndpoint {
    final private RegisterUserUseCase useCase;

    public RegisterUserEndpoint(RegisterUserUseCase useCase) {
        this.useCase = useCase;
    }

    public record RegisterRequest(String phoneNumber, String displayName) {}
    public record RegisterResponse(UUID id, String displayName) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            UserEntity user = useCase.execute(request.phoneNumber(), request.displayName());
            return ResponseEntity.ok(new RegisterResponse(user.getId(), user.getDisplayName()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}