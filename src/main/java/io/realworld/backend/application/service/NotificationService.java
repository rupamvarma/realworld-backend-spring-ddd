package io.realworld.backend.application.service;

import io.realworld.backend.application.dto.Mappers;
import io.realworld.backend.application.exception.UserNotFoundException;
import io.realworld.backend.application.util.BaseService;
import io.realworld.backend.domain.aggregate.notifications.Notification;
import io.realworld.backend.domain.aggregate.notifications.NotificationRepository;
import io.realworld.backend.domain.aggregate.user.UserRepository;
import io.realworld.backend.domain.service.AuthenticationService;
import io.realworld.backend.rest.api.NewNotificationRequestData;
import io.realworld.backend.rest.api.NotificationsApiDelegate;
import io.realworld.backend.rest.api.SingleNotificationResponseData;
import io.realworld.backend.rest.api.MultipleNotificationsResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class NotificationService extends BaseService implements NotificationsApiDelegate {

    private final AuthenticationService authenticationService;

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    @Autowired
    public NotificationService(AuthenticationService authenticationService, NotificationRepository notificationRepository, UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    @Override
    public ResponseEntity<SingleNotificationResponseData> createNotification(NewNotificationRequestData request) {
        final var currentUser = currentUserOrThrow();
        final var newNotificationData = request.getNotification();
        final var user =
                userRepository
                        .findById(newNotificationData.getReceiverId())
                        .orElseThrow(() -> new UserNotFoundException(String.valueOf(newNotificationData.getReceiverId())));
        final var notification = Mappers.fromNewNotificationData(newNotificationData, currentUser, user);
        notificationRepository.save(notification);
        return notificationResponse(notification);
    }
    private ResponseEntity<SingleNotificationResponseData> notificationResponse(Notification notification) {
        return ok(Mappers.toSingleNotificationResponse(notification));
    }


    @Override
    public ResponseEntity<MultipleNotificationsResponseData> getNotifications() {
        final var currentUser = currentUserOrThrow();
        final var user =
                userRepository
                        .findById(currentUser.getId())
                        .orElseThrow(() -> new UserNotFoundException("User not found"));
        return notificationsResponse(notificationRepository
                .findAllByReceiver(user).stream().collect(Collectors.toList()));
    }
    private ResponseEntity<MultipleNotificationsResponseData> notificationsResponse(
            List<Notification> notifications) {
        return ok(
                Mappers.toMultipleNotificationsResponseData(notifications));
    }
}
