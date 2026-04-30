package io.zalord.identity.features.register;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zalord.identity.internal.entities.UserEntity;

@RestController
@RequestMapping("/users")
public class RegisterEndpoint {
    final private RegisterUseCase useCase;

    public RegisterEndpoint(RegisterUseCase useCase) {
        this.useCase = useCase;
    }

    public record RegisterRequest(String phoneNumber, String displayName) {}
    public record RegisterResponse(UUID id, String displayName) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        UserEntity user = useCase.execute(request.phoneNumber(), request.displayName());
        return ResponseEntity.ok(new RegisterResponse(user.getId(), user.getDisplayName()));
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<String> handlePhoneNumberAlreadyExists(PhoneNumberAlreadyExistsException e) {
        // When the UseCase throws this exception, Spring automatically calls this method
        // instead of crashing or returning a 500 error.
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}