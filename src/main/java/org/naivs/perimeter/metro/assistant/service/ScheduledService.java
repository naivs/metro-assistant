package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import org.naivs.perimeter.metro.assistant.model.MetroProduct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ScheduledService {

    private final HttpClient httpClient;

    private final Map<String, MetroProduct> products = new HashMap<>();

    @Scheduled(initialDelay = 1000 * 10,
            fixedRate = 1000 * 60 * 60) // once per hour
    public void performUpdate() {
        products.keySet()
                .forEach(key -> {
                    MetroProduct item = httpClient.getItem(key);
                    products.replace(key, item);
                });
    }

    public List<MetroProduct> getProducts() {
        return new ArrayList<>(products.values());
    }

    public void addProduct(MetroProduct product) {
        products.put(product.getUrl(), product);
        MetroProduct item = httpClient.getItem(product.getUrl());
        products.replace(product.getUrl(), item);
    }
}
