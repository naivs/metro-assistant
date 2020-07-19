package org.naivs.perimeter.metro.assistant.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.model.MetroProduct;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HtmlStrategy implements ProbeStrategy {

    private final MetroClient metroClient;

    @Override
    public boolean probe(ProductEntity product) {
        try {
            MetroProduct metroProduct = metroClient.getItem(
                    product.getUrl()
            );

            product.setMetroId(metroProduct.getId());
            product.setName(metroProduct.getName());
            product.setPack(metroProduct.getMe());
            product.getProbes().add(extractProbe(metroProduct));

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private ProductProbeEntity extractProbe(MetroProduct metroProduct) {
        ProductProbeEntity probe = new ProductProbeEntity();
        probe.setRegularPrice(metroProduct.getRegularPrice());
        probe.setLeftPct(metroProduct.getLeftPct());

        metroProduct.getWholesalePrice()
                .forEach((key, value) -> probe.getWholesalePrice().put(key, value));

        return probe;
    }
}
