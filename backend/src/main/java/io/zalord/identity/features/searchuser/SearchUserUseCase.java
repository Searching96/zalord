package io.zalord.identity.features.searchuser;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zalord.identity.internal.repositories.UserRepository;
import io.zalord.identity.internal.repositories.UserRepository.SearchUserView;

@Service
public class SearchUserUseCase {

    private final UserRepository userRepository;
    
    public SearchUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public record SearchUserResult(UUID id, String displayName) {}

    @Transactional(readOnly = true)
    public SearchUserResult execute(String phoneNumber) {
        SearchUserView view = userRepository.findSearchUserViewByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new SearchUserNotFoundException(phoneNumber));

        return new SearchUserResult(view.getId(), view.getDisplayName());
    }
}