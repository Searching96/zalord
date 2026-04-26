package io.zalord.identity;

import java.util.UUID;

public interface IdentityAPI {
    boolean userExists(UUID userId);
}