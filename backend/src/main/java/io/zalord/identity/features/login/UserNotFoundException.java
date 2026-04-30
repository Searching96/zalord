package io.zalord.identity.features.login;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found. Please register first.");
    }
}
