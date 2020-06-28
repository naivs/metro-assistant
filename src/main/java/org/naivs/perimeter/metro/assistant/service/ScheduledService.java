package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduledService {

    private final ProductService productService;

    @Scheduled(initialDelay = 1000 * 10,
            fixedRate = 1000 * 60 * 60) // once per hour
    public void performUpdate() {
        List<ProductEntity> updatedProducts = productService.getProducts()
                .stream()
                .peek(productService::probe)
                .collect(Collectors.toList());
        productService.saveOrUpdateAll(updatedProducts);
    }
}
