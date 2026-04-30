package io.zalord.messaging.features;

public class ChatAccessDeniedException extends RuntimeException {
    public ChatAccessDeniedException() {
        super("Access Denied: User is not a member of this chat.");
    }
}
