package io.zalord.messaging.features.getuserchats;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zalord.identity.IdentityApi;
import io.zalord.messaging.internal.repositories.ChatMemberRepository;
import io.zalord.messaging.internal.repositories.ChatMemberRepository.ChatPartnerView;

@Service
public class GetUserChatsUseCase {

    private final ChatMemberRepository chatMemberRepository;
    private final IdentityApi identityApi;
    
    public GetUserChatsUseCase(ChatMemberRepository chatMemberRepository, IdentityApi identityApi) {
        this.chatMemberRepository = chatMemberRepository;
        this.identityApi = identityApi;
    }

    public record ChatListResult(UUID chatId, String chatName) {}
    
    @Transactional(readOnly = true)
    public List<ChatListResult> execute(UUID userId) {

        // 1. Get the list of chats and the partner's UUIDs
        List<ChatPartnerView> partners = chatMemberRepository.findChatPartners(userId);

        // 2. Extract just the partner UUIDs into a Set
        Set<UUID> partnerIds = partners
            .stream()
            .map(ChatPartnerView::getPartnerId)
            .collect(Collectors.toSet());
        
        // 3. Ask the Identity module to translate those UUIDs to display names
        Map<UUID, String> displayNames = identityApi.getDisplayNames(partnerIds);
        
        // 4. Build the final response
        return partners
            .stream()
            .map(partner -> {
                String name = displayNames.getOrDefault(partner.getPartnerId(), "Unknown User");
                return new ChatListResult(partner.getChatId(), name);
            })
            .toList();
    }
}