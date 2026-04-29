package io.zalord.messaging.features.sendmessage;

import io.zalord.identity.IdentityApi;
import io.zalord.messaging.features.sendmessage.SendMessageEndpoint.SendMessageResponse;
import io.zalord.messaging.internal.entities.MessageEntity;
import io.zalord.messaging.internal.repositories.ChatMemberRepository;
import io.zalord.messaging.internal.repositories.MessageRepository;

import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SendMessageUseCase {

    private final IdentityApi identityApi;
    private final ChatMemberRepository chatMemberRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public SendMessageUseCase(IdentityApi identityApi,
                              ChatMemberRepository chatMemberRepository,
                              MessageRepository messageRepository,
                              SimpMessagingTemplate messagingTemplate) {
        this.identityApi = identityApi;
        this.chatMemberRepository = chatMemberRepository;
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public SendMessageResponse execute(UUID chatId, UUID senderId, String content) {

        // 1. Cross-module call: Does this user even exist in the Identity system?
        if (!identityApi.userExists(senderId)) {
            throw new IllegalArgumentException("User does not exists.");
        }

        // 2. Authorization: Is the user actually a member of this chat room?
        if (!chatMemberRepository.existsById_ChatIdAndId_UserId(chatId, senderId)) {
            throw new IllegalArgumentException("User is not a member of this chat.");
        }

        // 3. Create and save the message
        MessageEntity newMessage = new MessageEntity(UUID.randomUUID(), chatId, senderId, content);
        MessageEntity savedMessage = messageRepository.saveAndFlush(newMessage);

        // 4. Map to DTO to prevent JSON serialization errors
        SendMessageResponse response = new SendMessageResponse(
            savedMessage.getId(), 
            savedMessage.getChatId(), 
            savedMessage.getSenderId(),
            savedMessage.getContent(),
            savedMessage.getCreatedAt() 
        );

        // 5. Broadcast the message to the specific chat room's topic
        messagingTemplate.convertAndSend("/topic/chats/" + chatId, response);

        return response;
    }
}