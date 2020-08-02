package org.naivs.perimeter.metro.assistant.component;

import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.model.Notification;

import java.util.Collection;

public interface NotificationHandler {

    void handle(ProductEntity product, Collection<Notification> notifications);
}
