package io.zalord.identity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IdentityApi {
    boolean userExists(UUID userId);
    Map<UUID, String> getDisplayNames(Set<UUID> userIds);
}