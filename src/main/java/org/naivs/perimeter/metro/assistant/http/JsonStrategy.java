package org.naivs.perimeter.metro.assistant.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.model.rest.PriceLevel;
import org.naivs.perimeter.metro.assistant.data.model.rest.Product;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
public class JsonStrategy implements ProbeStrategy {

    private final MetroClient metroClient;

    @Override
    public boolean probe(ProductEntity product) {
        try {
            Product rawProduct = metroClient.getProduct(product.getUrl().substring(
                    product.getUrl().lastIndexOf('/') + 1)
            );

            product.setMetroId(rawProduct.getId());
            product.setName(rawProduct.getName());
            product.setPack(rawProduct.getPacking().toString());

            ProductProbeEntity productProbeEntity = new ProductProbeEntity();
            productProbeEntity.setProduct(product);
            productProbeEntity.setLeftPct(rawProduct.getStock().getText().getPct());
            productProbeEntity.setRegularPrice(rawProduct.getPrices().getPrice());
            productProbeEntity.setTimestamp(LocalDateTime.now(ZoneId.of("+3")));

            ofNullable(rawProduct.getPrices().getLevels())
                    .ifPresent(priceLevels ->
                            productProbeEntity.setWholesalePrice(priceLevels.stream()
                                    .collect(Collectors.toMap(
                                            PriceLevel::getCount,
                                            PriceLevel::getPrice))
                            ));

            product.getProbes().add(productProbeEntity);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
