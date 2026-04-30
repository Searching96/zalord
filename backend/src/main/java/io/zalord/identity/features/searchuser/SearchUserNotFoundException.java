package io.zalord.identity.features.searchuser;

public class SearchUserNotFoundException extends RuntimeException {
    public SearchUserNotFoundException(String phoneNumber) {
        super("User with phone number " + phoneNumber + " not found.");
    }
}
