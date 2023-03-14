package io.realworld.backend.domain.aggregate.notifications;

import io.realworld.backend.domain.aggregate.role.Role;
import io.realworld.backend.domain.aggregate.user.User;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.FIELD)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private @NotNull Instant createdOn = Instant.now();
    private @NotNull String message = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private @NotNull User sender = new User("", "", "", new Role());
    private long receiverId = 0;

    public Notification(String message, User sender, long receiverId) {
        this.message = message;
        this.sender = sender;
        this.receiverId = receiverId;
    }

    public Notification() {

    }

    public long getId() {
        return id;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }
}
