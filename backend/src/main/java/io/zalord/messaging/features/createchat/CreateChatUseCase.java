package io.zalord.messaging.features.createchat;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.zalord.identity.IdentityAPI;
import io.zalord.messaging.internal.entities.ChatEntity;
import io.zalord.messaging.internal.entities.ChatMemberEntity;
import io.zalord.messaging.internal.repositories.ChatMemberRepository;
import io.zalord.messaging.internal.repositories.ChatRepository;

@Service
public class CreateChatUseCase {
    
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final IdentityAPI identityAPI;

    public CreateChatUseCase(ChatRepository chatRepository,
                             ChatMemberRepository chatMemberRepository,
                             IdentityAPI identityAPI) {
        this.chatRepository = chatRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.identityAPI = identityAPI;
    }

    public ChatEntity execute(List<UUID> participantsIds) {
        // 1. Validate all users exist
        for (UUID userId : participantsIds) {
            if (!identityAPI.userExists(userId)) {
                throw new IllegalArgumentException("User " + userId + " does not exist.");
            }
        }

        // 2. Create the Chat Header
        ChatEntity chat = new ChatEntity(UUID.randomUUID(), "DM");
        chatRepository.save(chat);

        // 3. Create the Chat Members (Links users to this chat)
        for (UUID userId : participantsIds) {
            ChatMemberEntity member = new ChatMemberEntity(chat.getId(), userId);
            chatMemberRepository.save(member);
        }

        return chat;
    }
}
