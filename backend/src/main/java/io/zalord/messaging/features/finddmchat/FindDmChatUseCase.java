package io.zalord.messaging.features.finddmchat;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zalord.messaging.internal.repositories.ChatMemberRepository;

@Service
public class FindDmChatUseCase {

    private static final String DM_TYPE = "DM";

    private final ChatMemberRepository chatMemberRepository;

    public FindDmChatUseCase(ChatMemberRepository chatMemberRepository) {
        this.chatMemberRepository = chatMemberRepository;
    }

    public record FindDmChatResult(UUID chatId, String type) {}

    @Transactional(readOnly = true)
    public FindDmChatResult execute(UUID userA, UUID userB) {
        List<UUID> chatIds = chatMemberRepository.findChatIdsByTypeAndParticipants(DM_TYPE, userA, userB);
        UUID chatId = chatIds.stream().findFirst()
            .orElseThrow(() -> new DmChatNotFoundException(userA, userB));

        return new FindDmChatResult(chatId, DM_TYPE);
    }
}
