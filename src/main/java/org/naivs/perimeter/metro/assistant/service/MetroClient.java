package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naivs.perimeter.metro.assistant.component.MetroProductMessageConverter;
import org.naivs.perimeter.metro.assistant.config.MetroConfig;
import org.naivs.perimeter.metro.assistant.model.MetroProduct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@Service
public class MetroClient {

    private final MetroConfig metroConfig;
    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
                new MetroProductMessageConverter());
    }

    public MetroProduct getItem(String itemUrl) {
        log.info("updating Product: " + itemUrl);
        try {
            ResponseEntity<MetroProduct> response = restTemplate.getForEntity(
                    new URI(metroConfig.getBaseUrl()).resolve(itemUrl),
                    MetroProduct.class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                log.info("updating Product response: [OK]");
                return response.getBody();
            } else {
                log.error("updating Product response: [FAIL]");
                throw new RuntimeException(
                        String.format("Response code: %s\n" +
                                "Response body:%s", response.getStatusCode(), response.getBody())
                );
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(
                    String.format("Item \"%s\" has been not obtained.", itemUrl), e);
        }
    }
}
