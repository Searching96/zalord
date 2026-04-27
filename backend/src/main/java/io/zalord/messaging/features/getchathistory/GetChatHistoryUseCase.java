package io.zalord.messaging.features.getchathistory;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.zalord.messaging.internal.entities.MessageEntity;
import io.zalord.messaging.internal.repositories.ChatMemberRepository;
import io.zalord.messaging.internal.repositories.MessageRepository;

@Service
public class GetChatHistoryUseCase {

    private final MessageRepository messageRepository;
    private final ChatMemberRepository chatMemberRepository;

    public GetChatHistoryUseCase(MessageRepository messageRepository,
                                 ChatMemberRepository chatMemberRepository) {
        this.messageRepository = messageRepository;
        this.chatMemberRepository = chatMemberRepository;
    }

    public List<MessageEntity> execute(UUID chatId, UUID requestingUserId) {
        // Check if the user requesting this history actually a member of the chat
        if (!chatMemberRepository.existsById_ChatIdAndId_UserId(chatId, requestingUserId)) {
            throw new IllegalArgumentException("Access Denied: User is not a member of this chat.");
        }

        return messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);
    }
}