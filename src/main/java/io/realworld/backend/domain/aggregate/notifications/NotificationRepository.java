package io.realworld.backend.domain.aggregate.notifications;

import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
}
