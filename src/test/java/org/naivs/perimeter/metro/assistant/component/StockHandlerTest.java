package org.naivs.perimeter.metro.assistant.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.enums.NotificationType;
import org.naivs.perimeter.metro.assistant.data.model.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
class StockHandlerTest {

    @InjectMocks
    private StockHandler stockHandler;

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
                ProductProbeBuilder.builder().build());
    }

    @Test
    void handleOneIsOutOfStock() {
        ProductProbeEntity probe = ProductProbeBuilder.builder()
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
        ProductProbeEntity probe = ProductProbeBuilder.builder()
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
        ProductProbeEntity probe = ProductProbeBuilder.builder()
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
        ProductProbeEntity probe = ProductProbeBuilder.builder()
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

    static class ProductProbeBuilder {
        private ProductProbeEntity probe;

        private ProductProbeBuilder() {
            probe = new ProductProbeEntity();
            probe.setId(1L);
            probe.setTimestamp(LocalDateTime.now());
            probe.setLeftPct(123);
            probe.setProduct(product);
            probe.setRegularPrice(125.04F);

            probe.getWholesalePrice()
                    .put(3, 115.00F);
            probe.getWholesalePrice()
                    .put(15, 101.50F);
        }

        static ProductProbeBuilder builder() {
            return new ProductProbeBuilder();
        }

        ProductProbeBuilder id(Long id) {
            probe.setId(id);
            return this;
        }

        ProductProbeBuilder timestamp(LocalDateTime when) {
            probe.setTimestamp(when);
            return this;
        }

        ProductProbeBuilder left(int left) {
            probe.setLeftPct(left);
            return this;
        }

        ProductProbeBuilder product(ProductEntity product) {
            probe.setProduct(product);
            return this;
        }

        ProductProbeBuilder regularPrice(float price) {
            probe.setRegularPrice(price);
            return this;
        }

        ProductProbeEntity build() {
            return probe;
        }
    }
}