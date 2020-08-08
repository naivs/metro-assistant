package org.naivs.perimeter.metro.assistant.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.enums.NotificationType;
import org.naivs.perimeter.metro.assistant.data.model.Notification;
import org.naivs.perimeter.metro.assistant.utils.ProductProbeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockHandlerTest {

    private final StockHandler stockHandler = new StockHandler();

    private static ProductEntity product;
    private static List<Notification> notifications;

    @BeforeEach
    void setUp() {
        notifications = new ArrayList<>();

        product = new ProductEntity();
        product.setName("Грудка куриная на кости ЯСНЫЕ ЗОРИ без кожи охлажденная");
        product.setPack("ME: 1 кг");
        product.setMetroId(322104L);

        product.getProbes().clear();
        product.getProbes().add(
                ProductProbeBuilder.builder(product).build());
    }

    @Test
    void handleOneIsOutOfStock() {
        ProductProbeEntity probe = ProductProbeBuilder.builder(product)
                .id(2L)
                .left(0)
                .build();
        product.getProbes().add(probe);

        stockHandler.handle(product, notifications);

        assertEquals(1, notifications.size());

        String expectedText = String.format(
                NotificationType.PRODUCT_ONE_OUT.getTemplate(),
                product.getName());
        assertEquals(expectedText, notifications.get(0).toString());
    }

    @Test
    void handleOneReturnInStock() {
        product.getProbes().get(0).setLeftPct(0);
        ProductProbeEntity probe = ProductProbeBuilder.builder(product)
                .id(2L)
                .left(134)
                .regularPrice(140.12F)
                .build();
        product.getProbes().add(probe);

        stockHandler.handle(product, notifications);

        assertEquals(1, notifications.size());

        String expectedText = String.format(
                NotificationType.PRODUCT_ONE_RETURN.getTemplate(),
                product.getName(), probe.getRegularPrice(), product.getPack());
        assertEquals(expectedText, notifications.get(0).toString());
    }

    @Test
    void handleSalesOut() {
        int rmPriceLvl = 3;
        ProductProbeEntity probe = ProductProbeBuilder.builder(product)
                .build();
        probe.getWholesalePrice().remove(rmPriceLvl);
        product.getProbes().add(probe);
        stockHandler.handle(product, notifications);

        assertEquals(1, notifications.size());

        String expectedText = String.format(
                NotificationType.PRODUCT_SALE_OUT.getTemplate(),
                product.getName(), rmPriceLvl + " шт.");
        assertEquals(expectedText, notifications.get(0).toString());
    }

    @Test
    void handleSalesReturn() {
        int retPriceLvl = 3;
        product.getProbes().get(0).getWholesalePrice().remove(retPriceLvl);
        ProductProbeEntity probe = ProductProbeBuilder.builder(product)
                .build();
        product.getProbes().add(probe);
        stockHandler.handle(product, notifications);

        assertEquals(1, notifications.size());

        String expectedText = String.format(
                NotificationType.PRODUCT_SALE_RETURN.getTemplate(),
                product.getName(), retPriceLvl + " шт.",
                probe.getWholesalePrice().get(retPriceLvl), product.getPack());
        assertEquals(expectedText, notifications.get(0).toString());
    }
}
