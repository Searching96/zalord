package io.zalord.messaging.features.getuserchats;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zalord.messaging.internal.repositories.ChatMemberRepository;

@Service
public class GetUserChatsUseCase {

    private final ChatMemberRepository chatMemberRepository;
    
    public GetUserChatsUseCase(ChatMemberRepository chatMemberRepository) {
        this.chatMemberRepository = chatMemberRepository;
    }

    @Transactional(readOnly = true)
    public List<UUID> execute(UUID userId) {
        return chatMemberRepository.findChatIdsByUserId(userId)
            .stream()
            .toList();
    }
}