package org.naivs.perimeter.metro.assistant.utils;

import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;

import java.time.LocalDateTime;

public class ProductProbeBuilder {
    private ProductProbeEntity probe;

    private ProductProbeBuilder(ProductEntity product) {
        probe = new ProductProbeEntity();
        probe.setId(1L);
        probe.setTimestamp(LocalDateTime.now());
        probe.setLeftPct(123);
        probe.setProduct(product);
        probe.setRegularPrice(125.04F);

        probe.getWholesalePrice()
                .put(3, 115.00F);
        probe.getWholesalePrice()
                .put(15, 101.50F);
    }

    public static ProductProbeBuilder builder(ProductEntity product) {
        return new ProductProbeBuilder(product);
    }

    public ProductProbeBuilder id(Long id) {
        probe.setId(id);
        return this;
    }

    public ProductProbeBuilder timestamp(LocalDateTime when) {
        probe.setTimestamp(when);
        return this;
    }

    public ProductProbeBuilder left(int left) {
        probe.setLeftPct(left);
        return this;
    }

    public ProductProbeBuilder product(ProductEntity product) {
        probe.setProduct(product);
        return this;
    }

    public ProductProbeBuilder regularPrice(float price) {
        probe.setRegularPrice(price);
        return this;
    }

    public ProductProbeEntity build() {
        return probe;
    }
}
