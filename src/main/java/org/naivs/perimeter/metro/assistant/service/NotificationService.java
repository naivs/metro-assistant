package org.naivs.perimeter.metro.assistant.service;

import org.naivs.perimeter.metro.assistant.data.enums.NotificationType;
import org.naivs.perimeter.metro.assistant.data.model.Notification;

public interface NotificationService {

    void sendNotification(Notification notification);

    class NotificationFactory {

        public static Notification productOneOut(String productName) {
            Notification notification = init(NotificationType.PRODUCT_ONE_OUT);
            notification.setArgs(new Object[]{productName});
            return notification;
        }

        public static Notification productOneReturn(String productName, float price, String pack) {
            Notification notification = init(NotificationType.PRODUCT_ONE_RETURN);
            notification.setArgs(new Object[]{productName, price, pack});
            return notification;
        }

        public static Notification produtSaleOut(String productName, String pack) {
            Notification notification = init(NotificationType.PRODUCT_SALE_OUT);
            notification.setArgs(new Object[]{productName, pack});
            return notification;
        }

        public static Notification produtSaleReturn(String productName, String pack) {
            Notification notification = init(NotificationType.PRODUCT_SALE_RETURN);
            notification.setArgs(new Object[]{productName, pack});
            return notification;
        }

        public static Notification priceIncreased(float deltaPrice, String productName,
                                                  String pack, float newPrice) {
            Notification notification = init(NotificationType.PRICE_INCREASED);
            notification.setArgs(new Object[]{deltaPrice, productName, pack, newPrice});
            return notification;
        }

        public static Notification priceDecreased(float deltaPrice, String productName,
                                                  String pack, float newPrice) {
            Notification notification = init(NotificationType.PRICE_DECREASED);
            notification.setArgs(new Object[]{deltaPrice, productName, pack, newPrice});
            return notification;
        }

        public static Notification priceLevelOne(String productName, float price, String pack, float deltaPrice) {
            Notification notification = init(NotificationType.PRICE_LEVEL_ONE);
            notification.setArgs(new Object[]{productName, price, pack, deltaPrice});
            return notification;
        }

        public static Notification priceLevelTwo(String productName, float price, String pack, float deltaPrice) {
            Notification notification = init(NotificationType.PRICE_LEVEL_TWO);
            notification.setArgs(new Object[]{productName, price, pack, deltaPrice});
            return notification;
        }

        public static Notification priceLevelThree(String productName, float price, String pack, float deltaPrice) {
            Notification notification = init(NotificationType.PRICE_LEVEL_THREE);
            notification.setArgs(new Object[]{productName, price, pack, deltaPrice});
            return notification;
        }

        private static Notification init(NotificationType type) {
            Notification notification = new Notification();
            notification.setType(type);
            return notification;
        }
    }
}
