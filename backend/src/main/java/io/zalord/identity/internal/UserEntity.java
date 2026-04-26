package io.zalord.identity.internal;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "identity")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    private UUID id;

    @Column(name = "phone_number", unique = true, nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    public UserEntity(UUID id, String phoneNumber, String displayName) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
    }
}