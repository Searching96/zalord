package io.zalord.messaging.features.sendmessage;

import io.zalord.identity.IdentityAPI;
import io.zalord.messaging.internal.entities.MessageEntity;
import io.zalord.messaging.internal.repositories.ChatMemberRepository;
import io.zalord.messaging.internal.repositories.MessageRepository;

import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SendMessageUseCase {

    private final IdentityAPI identityAPI;
    private final ChatMemberRepository chatMemberRepository;
    private final MessageRepository messageRepository;

    public SendMessageUseCase(IdentityAPI identityAPI,
                              ChatMemberRepository chatMemberRepository,
                              MessageRepository messageRepository) {
        this.identityAPI = identityAPI;
        this.chatMemberRepository = chatMemberRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public MessageEntity execute(UUID chatId, UUID senderId, String content) {

        // 1. Cross-module call: Does this user even exist in the Identity system?
        if (!identityAPI.userExists(senderId)) {
            throw new IllegalArgumentException("User does not exists.");
        }

        // 2. Authorization: Is the user actually a member of this chat room?
        if (!chatMemberRepository.existsById_ChatIdAndId_UserId(chatId, senderId)) {
            throw new IllegalArgumentException("User is not a member of this chat.");
        }

        // 3. Create and save the message
        MessageEntity newMessage = new MessageEntity(UUID.randomUUID(), chatId, senderId, content);

        return messageRepository.save(newMessage);
    }
}