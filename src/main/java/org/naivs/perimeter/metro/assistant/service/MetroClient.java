package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naivs.perimeter.metro.assistant.component.MetroProductMessageConverter;
import org.naivs.perimeter.metro.assistant.config.MetroConfig;
import org.naivs.perimeter.metro.assistant.data.model.MetroProduct;
import org.naivs.perimeter.metro.assistant.data.model.rest.Product;
import org.naivs.perimeter.metro.assistant.data.model.rest.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MetroClient {

    private final MetroConfig metroConfig;
    private RestTemplate restTemplate;

    private URI apiUri;
    private final static String PRODUCT_URL = "products";

    @PostConstruct
    private void init() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
                new MetroProductMessageConverter());
        apiUri = UriComponentsBuilder
                .fromHttpUrl(metroConfig.getApiHost())
                .pathSegment(metroConfig.getApiBaseUrl())
                .build()
                .toUri();
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

    public Product getProduct(String slug) {
        Map<String, String> vars = new HashMap<>();
        vars.put("slug", slug);
        String productUrl = UriComponentsBuilder.fromUri(apiUri)
                .pathSegment(PRODUCT_URL)
                .queryParam("slug", slug)
                .build().toString();

        ResponseEntity<Response> response =
                restTemplate.getForEntity(productUrl, Response.class);
        if (response.getBody() != null && response.getStatusCode().equals(HttpStatus.OK)) {
            List<Product> products = response.getBody().getData().getData();
            return products.size() > 0 ? products.get(0) : null;
        } else {
            log.error(String.format("Item \"%s\" has been not obtained.", slug));
            return null;
        }
    }
}
