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
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private @NotNull User receiver = new User("", "", "", new Role());
    private long senderId = 0;

    public Notification(String message, User receiver, long senderId) {
        this.message = message;
        this.receiver = receiver;
        this.senderId = senderId;
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

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }
}
