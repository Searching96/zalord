package io.zalord.identity.internal.entities;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "users", schema = "identity")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Column(name = "phone_number", unique = true, nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    public UserEntity(UUID id, String phoneNumber, String displayName) {
        super(id);
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
    }
}