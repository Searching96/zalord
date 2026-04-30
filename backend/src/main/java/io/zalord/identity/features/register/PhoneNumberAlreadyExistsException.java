package io.zalord.identity.features.register;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException(String phoneNumber) {
        super("Phone number " + phoneNumber + " is already registered.");
    }
}