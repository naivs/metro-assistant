package org.naivs.perimeter.metro.assistant.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.enums.NotificationType;
import org.naivs.perimeter.metro.assistant.data.model.Notification;
import org.naivs.perimeter.metro.assistant.utils.ProductGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PriceDeviationsHandlerTest {

    private final PriceDeviationsHandler handler = new PriceDeviationsHandler();

    private static ProductEntity product;
    private static List<Notification> notifications;

    @BeforeEach
    void setUp() {
        notifications = new ArrayList<>();
        product = ProductGenerator.generateProducts(1, false).get(0);
        product.getProbes().addAll(
                ProductGenerator.generateProbes(2, product, false)
        );
    }

    @Test
    void regularPriceIncreased() {
        List<ProductProbeEntity> probes = product.getProbes();
        ProductProbeEntity prevProbe = probes.get(0);
        ProductProbeEntity lastProbe = probes.get(1);
        prevProbe.getWholesalePrice().clear();
        lastProbe.getWholesalePrice().clear();

        prevProbe.setRegularPrice(14.30F);
        lastProbe.setRegularPrice(20.00F);
        handler.handle(product, notifications);

        assertEquals(1, notifications.size());
        assertEquals(String.format(
                NotificationType.PRICE_INCREASED.getTemplate(),
                lastProbe.getRegularPrice() - prevProbe.getRegularPrice(),
                product.getName(),
                product.getPack(),
                lastProbe.getRegularPrice()), notifications.get(0).toString());
    }

    @Test
    void regularPriceDecreased() {
        List<ProductProbeEntity> probes = product.getProbes();
        ProductProbeEntity prevProbe = probes.get(0);
        ProductProbeEntity lastProbe = probes.get(1);
        prevProbe.getWholesalePrice().clear();
        lastProbe.getWholesalePrice().clear();

        prevProbe.setRegularPrice(20.00F);
        lastProbe.setRegularPrice(14.30F);
        handler.handle(product, notifications);

        assertEquals(1, notifications.size());
        assertEquals(String.format(
                NotificationType.PRICE_DECREASED.getTemplate(),
                prevProbe.getRegularPrice() - lastProbe.getRegularPrice(),
                product.getName(),
                product.getPack(),
                lastProbe.getRegularPrice()), notifications.get(0).toString());
    }

    @Test
    void wholesalePriceIncreased() {
        List<ProductProbeEntity> probes = product.getProbes();
        ProductProbeEntity prevProbe = probes.get(0);
        ProductProbeEntity lastProbe = probes.get(1);

        prevProbe.setRegularPrice(20.00F);
        lastProbe.setRegularPrice(20.00F);
        prevProbe.getWholesalePrice().clear();
        prevProbe.getWholesalePrice().put(3, 30.42F);
        lastProbe.getWholesalePrice().clear();
        lastProbe.getWholesalePrice().put(3, 34.00F);

        handler.handle(product, notifications);

        assertEquals(1, notifications.size());
        assertEquals(String.format(
                NotificationType.PRICE_INCREASED.getTemplate(),
                lastProbe.getWholesalePrice().get(3) - prevProbe.getWholesalePrice().get(3),
                product.getName(),
                product.getPack() + " x" + 3,
                lastProbe.getWholesalePrice().get(3)), notifications.get(0).toString());
    }

    @Test
    void wholesalePriceDecreased() {
        List<ProductProbeEntity> probes = product.getProbes();
        ProductProbeEntity prevProbe = probes.get(0);
        ProductProbeEntity lastProbe = probes.get(1);

        prevProbe.setRegularPrice(20.00F);
        lastProbe.setRegularPrice(20.00F);
        prevProbe.getWholesalePrice().clear();
        prevProbe.getWholesalePrice().put(3, 21.50F);
        lastProbe.getWholesalePrice().clear();
        lastProbe.getWholesalePrice().put(3, 20.00F);

        handler.handle(product, notifications);

        assertEquals(1, notifications.size());
        assertEquals(String.format(
                NotificationType.PRICE_DECREASED.getTemplate(),
                prevProbe.getWholesalePrice().get(3) - lastProbe.getWholesalePrice().get(3),
                product.getName(),
                product.getPack() + " x" + 3,
                lastProbe.getWholesalePrice().get(3)), notifications.get(0).toString());
    }

    @Test
    void noNotifications() {
        List<ProductProbeEntity> probes = product.getProbes();
        ProductProbeEntity prevProbe = probes.get(0);
        ProductProbeEntity lastProbe = probes.get(1);
        prevProbe.getWholesalePrice().clear();
        lastProbe.getWholesalePrice().clear();

        prevProbe.setRegularPrice(20.00F);
        lastProbe.setRegularPrice(20.00F);
        handler.handle(product, notifications);

        assertTrue(notifications.isEmpty());
    }
}
