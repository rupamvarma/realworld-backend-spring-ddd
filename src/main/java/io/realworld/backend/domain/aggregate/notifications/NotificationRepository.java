package io.realworld.backend.domain.aggregate.notifications;

import io.realworld.backend.domain.aggregate.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> findAllByReceiver(User user);
}
