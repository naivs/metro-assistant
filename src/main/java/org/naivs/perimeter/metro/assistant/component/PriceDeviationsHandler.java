package org.naivs.perimeter.metro.assistant.component;

import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.model.Notification;
import org.naivs.perimeter.metro.assistant.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class PriceDeviationsHandler implements NotificationHandler {

    @Override
    public void handle(ProductEntity product, Collection<Notification> notifications) {
        List<ProductProbeEntity> probes = product.getProbes();

        if (probes.size() > 1) {
            ProductProbeEntity lastProbe = probes.get(
                    product.getProbes().size() - 1);
            ProductProbeEntity previousProbe = probes.get(
                    product.getProbes().size() - 2);

            isPriceIncreased(previousProbe, lastProbe)
                    .ifPresent(notifications::add);
            isPriceDecreased(previousProbe, lastProbe)
                    .ifPresent(notifications::add);
            notifications.addAll(isWsPriceChanged(
                    previousProbe, lastProbe));
        }
    }

    private Optional<Notification> isPriceIncreased(ProductProbeEntity prev,
                                                  ProductProbeEntity last) {
        Float prevPrice = prev.getRegularPrice();
        Float lastPrice = last.getRegularPrice();
        if (lastPrice.compareTo(prevPrice) > 0) {
            return Optional.of(
                    NotificationService.NotificationFactory.priceIncreased(
                            lastPrice - prevPrice, last.getProduct().getName(),
                            last.getProduct().getPack(), lastPrice)
            );
        }

        return Optional.empty();
    }

    private Optional<Notification> isPriceDecreased(ProductProbeEntity prev,
                                                    ProductProbeEntity last) {
        Float prevPrice = prev.getRegularPrice();
        Float lastPrice = last.getRegularPrice();
        if (prevPrice.compareTo(lastPrice) > 0) {
            return Optional.of(
                    NotificationService.NotificationFactory.priceDecreased(
                            prevPrice - lastPrice, last.getProduct().getName(),
                            last.getProduct().getPack(), lastPrice)
            );
        }

        return Optional.empty();
    }

    private List<Notification> isWsPriceChanged(ProductProbeEntity prev,
                                                  ProductProbeEntity last) {
        List<Notification> notifications = new ArrayList<>();

        Map<Integer, Float> prevWsp = prev.getWholesalePrice();
        Map<Integer, Float> lastWsp = last.getWholesalePrice();

        Stream.concat(prevWsp.keySet().stream(), lastWsp.keySet().stream())
                .distinct()
                .filter(key -> prevWsp.containsKey(key) && lastWsp.containsKey(key))
                .forEach(key -> {
                    float prevPrice = prevWsp.get(key);
                    float lastPrice = lastWsp.get(key);

                    if (lastPrice > prevPrice) {
                        notifications.add(
                                NotificationService.NotificationFactory
                                        .priceIncreased(
                                                lastPrice - prevPrice,
                                                last.getProduct().getName(),
                                                last.getProduct().getPack() + " x" + key,
                                                lastPrice));
                    } else if (lastPrice < prevPrice) {
                        notifications.add(
                                NotificationService.NotificationFactory
                                        .priceDecreased(
                                                prevPrice - lastPrice,
                                                last.getProduct().getName(),
                                                last.getProduct().getPack() + " x" + key,
                                                lastPrice));
                    }
                });

        return notifications;
    }
}
