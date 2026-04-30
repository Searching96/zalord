package io.zalord.messaging.features.getchathistory;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zalord.messaging.features.ChatAccessDeniedException;
import io.zalord.messaging.internal.repositories.ChatMemberRepository;
import io.zalord.messaging.internal.repositories.MessageRepository;
import io.zalord.messaging.internal.repositories.MessageRepository.MessageHistoryView;

@Service
public class GetChatHistoryUseCase {

    private final MessageRepository messageRepository;
    private final ChatMemberRepository chatMemberRepository;

    public GetChatHistoryUseCase(MessageRepository messageRepository,
                                 ChatMemberRepository chatMemberRepository) {
        this.messageRepository = messageRepository;
        this.chatMemberRepository = chatMemberRepository;
    }

    @Transactional(readOnly = true)
    public List<MessageHistoryView> execute(UUID chatId, UUID requestingUserId) {
        // Check if the user requesting this history actually a member of the chat
        if (!chatMemberRepository.existsById_ChatIdAndId_UserId(chatId, requestingUserId)) {
            throw new ChatAccessDeniedException();
        }

        return messageRepository.findMessageHistoryByChatIdOrderByCreatedAtAsc(chatId);
    }
}