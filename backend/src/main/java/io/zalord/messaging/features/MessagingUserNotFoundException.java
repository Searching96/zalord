package io.zalord.messaging.features;

import java.util.UUID;

public class MessagingUserNotFoundException extends RuntimeException {
    public MessagingUserNotFoundException(UUID userId) {
        super("User " + userId + " does not exist.");
    }
}
