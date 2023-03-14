package io.realworld.backend.application.service;

import io.realworld.backend.application.dto.Mappers;
import io.realworld.backend.application.util.BaseService;
import io.realworld.backend.domain.aggregate.article.Article;
import io.realworld.backend.domain.aggregate.favourite.ArticleFavouriteId;
import io.realworld.backend.domain.aggregate.notifications.Notification;
import io.realworld.backend.domain.aggregate.notifications.NotificationRepository;
import io.realworld.backend.domain.aggregate.user.UserRepository;
import io.realworld.backend.domain.service.AuthenticationService;
import io.realworld.backend.rest.api.NewNotificationRequestData;
import io.realworld.backend.rest.api.NotificationsApiDelegate;
import io.realworld.backend.rest.api.SingleArticleResponseData;
import io.realworld.backend.rest.api.SingleNotificationResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

        final var newArticleData = request.getNotification();
        final var notification = Mappers.fromNewNotificationData(newArticleData, currentUser);
        notificationRepository.save(notification);

        return notificationResponse(notification);
    }
    private ResponseEntity<SingleNotificationResponseData> notificationResponse(Notification notification) {
        return ok(Mappers.toSingleNotificationResponse(notification));
    }
}
