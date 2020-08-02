package org.naivs.perimeter.metro.assistant.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.model.Notification;
import org.naivs.perimeter.metro.assistant.service.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class NotificationHandlerAspect {

    @Qualifier("telegramNotificationService")
    private final NotificationService notificationService;
    private final List<NotificationHandler> notificationHandlers;

    @AfterReturning(
            pointcut = "execution(public boolean org.naivs.perimeter.metro.assistant.http.MetroClient.poll(*))" +
                    " && args(product)",
            returning = "isSuccess",
            argNames = "isSuccess, product"
    )
    public void poll(boolean isSuccess, ProductEntity product) {
        Set<Notification> notifications = new HashSet<>();

        if (isSuccess) {
            // handle
            notificationHandlers.forEach(handler -> handler.handle(product, notifications));
        } else {
            log.error("Product {} not updated.", product);
            // todo: send notification
        }

        notifications.forEach(notificationService::sendNotification);
    }
}
