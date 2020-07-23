package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduledService {

    private final ProductService productService;

    @Transactional
    @Scheduled(initialDelay = 1000 * 10,
            fixedDelayString = "${metro.poll-interval}")
    // TODO: MILLIS ARITHMETIC LEAVE HERE (set in properties minutes or hours only)
    public void performUpdate() {
        long start = System.currentTimeMillis();
        log.info("product update procedure started..");
        productService.pollAllProducts();
        log.info(String.format(
                "product update procedure finished.. (at %f sec.)",
                (System.currentTimeMillis() - start) / 1000F));
    }
}
