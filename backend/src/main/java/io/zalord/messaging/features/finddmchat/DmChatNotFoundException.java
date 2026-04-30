package io.zalord.messaging.features.finddmchat;

import java.util.UUID;

public class DmChatNotFoundException extends RuntimeException {
    public DmChatNotFoundException(UUID userA, UUID userB) {
        super("No DM chat exists for users " + userA + " and " + userB + ".");
    }
}
