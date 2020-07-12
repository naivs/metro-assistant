package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduledService {

    private final ProductService productService;
    private final ProbeStrategy probeStrategy;

    @Scheduled(initialDelay = 1000 * 10,
            fixedDelayString = "${metro.poll-interval}")
    public void performUpdate() {
        long start = System.currentTimeMillis();
        log.info("product update procedure started..");

        log.info("product polling started..");
        List<ProductEntity> updatedProducts = productService.getProducts()
                .stream()
                .filter(probeStrategy::probe)
                .collect(Collectors.toList());
        log.info("product polling finished..");
        log.info("product saving..");
        productService.saveOrUpdateAll(updatedProducts);

        log.info(String.format(
                "product update procedure finished.. (at %f sec.)",
                (System.currentTimeMillis() - start) / 1000F));
    }
}
