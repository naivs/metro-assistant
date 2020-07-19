package org.naivs.perimeter.metro.assistant.utils;

import com.sun.istack.Nullable;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Optional.ofNullable;

public class ProductGenerator {

    private static final Random random = ThreadLocalRandom.current();
    // TODO: AVOID OF COLLISIONS
    public static List<ProductEntity> generateProducts(int count, boolean withId) {
        Set<ProductEntity> products = new HashSet<>();
        for (int i = 0; i < count; i++) {
            while (products.size() < i+1) {
                products.add(getProduct(withId));
            }
        }
        return new ArrayList<>(products);
    }

    public static List<ProductProbeEntity> generateProbes(int count,
                                                          @Nullable ProductEntity product,
                                                          boolean withId
    ) {
        Set<ProductProbeEntity> probes = new HashSet<>();
        for (int i = 0; i < count; i++) {
            while (probes.size() < i+1) {
                probes.add(getProbe(product, withId));
            }
        }
        return new ArrayList<>(probes);
    }

    private static ProductEntity getProduct(boolean withId) {
        int surrogate = random.nextInt(10000 - 1) + 1;

        ProductEntity product = new ProductEntity();
        product.setId(withId ? (long) random.nextInt(10000) : null);
        product.setName("test product: " + surrogate);
        product.setMetroId((long) surrogate);
        product.setUrl("http://metro-url.com/some_path/" + surrogate);
        product.setPack("ME: 1 шт");
        return product;
    }

    private static ProductProbeEntity getProbe(@Nullable ProductEntity product,
                                               boolean withId
    ) {
        ProductProbeEntity probe = new ProductProbeEntity();
        probe.setId(withId ? (long) random.nextInt(10000) + 1 : null);
        probe.setTimestamp(randomDateTime(null));
        ofNullable(product).ifPresent(probe::setProduct);
        probe.setLeftPct(random.nextInt(100));
        probe.setRegularPrice((float)(random.nextInt(1000) + 1) + random.nextFloat());

        Map<Integer, Float> wholesales = probe.getWholesalePrice();

        int rounds = random.nextInt(3) + 1;
        for (int i = 0; i < rounds; i++) {
            wholesales.put(random.nextInt(15) + 1,
                    probe.getRegularPrice() + random.nextInt(50));
        }
        return probe;
    }

    private static LocalDateTime randomDateTime(@Nullable Set<LocalDateTime> uniqueScope) {
        LocalDateTime randomDateTime;

        do {
            long minDay = LocalDateTime
                    .of(1970, 1, 1, 1, 1, 1)
                    .toEpochSecond(ZoneOffset.UTC);
            long maxDay = LocalDateTime
                    .of(2020, 3, 31, 23, 59, 59)
                    .toEpochSecond(ZoneOffset.UTC);
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            randomDateTime = LocalDateTime
                    .ofEpochSecond(randomDay, 0, ZoneOffset.UTC);
        } while (uniqueScope != null && uniqueScope.contains(randomDateTime));

        return randomDateTime;
    }
}
