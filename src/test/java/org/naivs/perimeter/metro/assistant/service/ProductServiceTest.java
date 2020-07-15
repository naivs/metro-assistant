package org.naivs.perimeter.metro.assistant.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.model.PriceHistoryModel;
import org.naivs.perimeter.metro.assistant.data.repo.ProductProbeRepository;
import org.naivs.perimeter.metro.assistant.data.repo.ProductRepository;
import org.naivs.perimeter.metro.assistant.generator.ProductGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@MockitoSettings
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductProbeRepository productProbeRepository;
    @InjectMocks
    private ProductService productService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void getPriceHistory() { // TODO: MAKE THIS TEST STABILITY
        final int PROBES_COUNT = 80;

        ProductEntity product = ProductGenerator.generateProducts(1, true).get(0);
        List<ProductProbeEntity> productProbes = ProductGenerator
                .generateProbes(PROBES_COUNT, product, true)
                .stream()
                .sorted(Comparator.comparing(ProductProbeEntity::getTimestamp))
                .collect(Collectors.toList());

        when(productRepository.findById(eq(product.getId())))
                .thenReturn(Optional.of(product));
        when(productProbeRepository.findByProductIdAndProbesAfter(eq(product.getId()), any(LocalDateTime.class)))
                .thenReturn(productProbes);

        PriceHistoryModel priceHistory = productService.getPriceHistory(product.getId());
        assertNotNull(priceHistory);

        List<String> historyColumns = priceHistory.getColumns();
        assertNotNull(historyColumns);
        List<String> expectedColumns = productProbes.stream()
                .flatMap(productProbeEntity -> productProbeEntity.getWholesalePrice().keySet().stream())
                .distinct()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.toList());
        assertEquals(expectedColumns.size(), historyColumns.size());
        // columns must be sorted and equals
        assertEquals(expectedColumns, historyColumns);

        // string timestamp to price values list
        Map<String, List<Float>> historyRows = priceHistory.getRows();
        assertNotNull(historyRows);
        assertEquals(PROBES_COUNT, historyRows.size());
        // all price lists must be the same value
        Iterator<List<Float>> i = historyRows.values().iterator();
        int listSize = i.next().size();
        i.forEachRemaining(prices ->
                assertEquals(listSize, prices.size()));
        // must be sorted by timestamp
        Set<String> expectedTimestamps = historyRows.keySet().stream()
                .map(rawStamp -> LocalDateTime.parse(rawStamp, DATE_TIME_FORMATTER))
                .sorted(LocalDateTime::compareTo)
                .map(DATE_TIME_FORMATTER::format)
                .collect(Collectors.toCollection(TreeSet::new));
        assertEquals(expectedTimestamps, historyRows.keySet());
    }
}