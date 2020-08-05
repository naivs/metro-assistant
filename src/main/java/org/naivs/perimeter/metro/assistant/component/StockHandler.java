package org.naivs.perimeter.metro.assistant.component;

import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.model.Notification;
import org.naivs.perimeter.metro.assistant.service.NotificationService;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class StockHandler implements NotificationHandler {

    @Override
    public void handle(ProductEntity product, Collection<Notification> notifications) {
        List<ProductProbeEntity> probes = product.getProbes();

        if (probes.size() > 1) {
            ProductProbeEntity lastProbe = probes.get(
                    product.getProbes().size() - 1);
            ProductProbeEntity previousProbe = probes.get(
                    product.getProbes().size() - 2);

            isOutOfStock(previousProbe, lastProbe)
                    .ifPresent(notifications::add);
            notifications.addAll(
                    isSalesOut(previousProbe, lastProbe));
        }
    }

    private Optional<Notification> isOutOfStock(ProductProbeEntity prev,
                                                ProductProbeEntity last) {
        if (prev.getLeftPct().compareTo(last.getLeftPct()) != 0) {
            return Optional.of(prev.getLeftPct() == 0
                    ? NotificationService.NotificationFactory.productOneReturn(
                            last.getProduct().getName(),
                            last.getRegularPrice(),
                            last.getProduct().getPack())
                    : NotificationService.NotificationFactory.productOneOut(
                            last.getProduct().getName())
            );
        }
        return Optional.empty();
    }

    @NotNull
    private Collection<Notification> isSalesOut(ProductProbeEntity prev,
                                                ProductProbeEntity last) {
        Set<Notification> notifications = new HashSet<>();

        // it's sorted maps
        Map<Integer, Float> prevWsp = prev.getWholesalePrice();
        Map<Integer, Float> lastWsp = last.getWholesalePrice();

        Stream.concat(prevWsp.keySet().stream(), lastWsp.keySet().stream())
                .distinct()
                .filter(key -> !prevWsp.containsKey(key) || !lastWsp.containsKey(key))
                .forEach(key -> {
                    // false -> out;   true -> returned
                    if (lastWsp.containsKey(key)) {
                        notifications.add(
                                NotificationService.NotificationFactory.produtSaleReturn(
                                        last.getProduct().getName(),
                                        key + " шт.",
                                        lastWsp.get(key),
                                        last.getProduct().getPack()));
                    } else {
                        notifications.add(
                                NotificationService.NotificationFactory.produtSaleOut(
                                        prev.getProduct().getName(),
                                        key + " шт."));
                    }
                });

        return notifications;
    }
}
