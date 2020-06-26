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

@Slf4j
@RequiredArgsConstructor
@Service
public class HttpClient {

    private final MetroConfig metroConfig;
    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
                new MetroProductMessageConverter());
    }

    public MetroProduct getItem(String itemUrl) {
        try {
            ResponseEntity<MetroProduct> response = restTemplate.getForEntity(
                    new URI(metroConfig.getBaseUrl()).resolve(itemUrl),
                    MetroProduct.class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                return response.getBody();
            } else {
                String msg = String.format("Response code: %s\n" +
                        "Response body:%s", response.getStatusCode(), response.getBody());
                log.error(msg);
                throw new RuntimeException(msg);
            }
        } catch (Exception e) {
            String msg = String.format("Item \"%s\" has been not obtained.", itemUrl);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
