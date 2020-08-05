package org.naivs.perimeter.metro.assistant.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naivs.perimeter.metro.assistant.config.MetroConfig;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.model.external.PriceLevel;
import org.naivs.perimeter.metro.assistant.data.model.external.Product;
import org.naivs.perimeter.metro.assistant.data.model.external.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

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
                new HttpPageMessageConverter()
        );
        apiUri = UriComponentsBuilder
                .fromHttpUrl(metroConfig.getApiHost())
                .pathSegment(metroConfig.getApiBaseUrl())
                .build()
                .toUri();
    }

    public boolean poll(ProductEntity product) {
        try {
            // point of choose request type html|json
            pollForJson(product);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private void pollForHtml(ProductEntity productEntity) {
        log.info("updating Product: " + productEntity.getUrl());
        try {
            ResponseEntity<ProductEntity> response = restTemplate.getForEntity(
                    new URI(productEntity.getUrl()), ProductEntity.class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                log.info("updating Product response: [OK]");
                ProductEntity product = response.getBody();
                // fixme: map to product arg
            } else {
                log.error("updating Product response: [FAIL]");
                log.error(
                        String.format("Response code: %s\n" +
                                "Response body:%s", response.getStatusCode(), response.getBody())
                );
            }
        } catch (URISyntaxException e) {
            log.error(
                    String.format("Item \"%s\" has been not obtained.", productEntity.getUrl()), e);
        }
    }

    private void pollForJson(ProductEntity productEntity) {
        String slug = productEntity.getUrl().substring(
                productEntity.getUrl().lastIndexOf('/') + 1);

        String productUrl = UriComponentsBuilder.fromUri(apiUri)
                .pathSegment(PRODUCT_URL)
                .queryParam("slug", slug)
                .build().toString();

        ResponseEntity<Response> response =
                restTemplate.getForEntity(productUrl, Response.class);
        if (response.getBody() != null && response.getStatusCode().equals(HttpStatus.OK)) {
            List<Product> products = response.getBody().getData().getData();
            Product product = products.size() > 0 ? products.get(0) : null;
            if (product == null) {
                log.error(String.format("For \"%s\" all elements in response is null.", slug));
                return;
            }

            productEntity.setMetroId(product.getId());
            productEntity.setName(product.getName());
            productEntity.setPack(product.getPacking().toString());

            ProductProbeEntity productProbeEntity = new ProductProbeEntity();
            productProbeEntity.setProduct(productEntity);
            productProbeEntity.setLeftPct(product.getStock().getText().getPct());
            productProbeEntity.setRegularPrice(product.getPrices().getPrice());
            productProbeEntity.setTimestamp(LocalDateTime.now(ZoneId.of("+3")));

            ofNullable(product.getPrices().getLevels())
                    .ifPresent(priceLevels ->
                            productProbeEntity.setWholesalePrice(priceLevels.stream()
                                    .collect(Collectors.toMap(
                                            PriceLevel::getCount,
                                            PriceLevel::getPrice))
                            ));

            productEntity.getProbes().add(productProbeEntity);
        } else {
            log.error(String.format("Item \"%s\" has been not obtained.", slug));
        }
    }
}
