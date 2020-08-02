package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import org.naivs.perimeter.metro.assistant.data.model.Notification;
import org.springframework.stereotype.Service;

@Service("telegramNotificationService")
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {

    private final BotService botService;

    @Override
    public void sendNotification(Notification notification) {
        botService.sendMessage(notification.toString());
    }
}
