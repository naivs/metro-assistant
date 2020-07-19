package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.enums.HistoryRange;
import org.naivs.perimeter.metro.assistant.data.model.PriceHistoryModel;
import org.naivs.perimeter.metro.assistant.data.repo.ProductProbeRepository;
import org.naivs.perimeter.metro.assistant.data.repo.ProductRepository;
import org.naivs.perimeter.metro.assistant.http.ProbeStrategy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductProbeRepository productProbeRepository;
    private final ProbeStrategy probeStrategy;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    public ProductEntity addProduct(String productUrl) {
        ProductEntity product = new ProductEntity();
        product.setUrl(productUrl);

        return probeStrategy.probe(product) ? productRepository.saveAndFlush(product) : null;
    }

    public List<ProductEntity> getProducts() {
        return productRepository.findAll();
    }

    public PriceHistoryModel getPriceHistory(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with \"id\"=" + productId));
        List<ProductProbeEntity> probes = productProbeRepository
                .findByProductIdAndProbesAfter(productId, HistoryRange.QUARTER.get());
        product.setProbes(probes);

        Map<LocalDateTime, Map<Integer, Float>> prices = getPrices(probes);

        // get columns
        List<String> columns = probesToChartColumns(prices);

        // get rows
        Map<String, List<Float>> rows = probesToChartRows(prices, columns);

        PriceHistoryModel priceHistory = new PriceHistoryModel();
        priceHistory.setColumns(columns);
        priceHistory.setRows(rows);

        return priceHistory;
    }

    public void saveOrUpdateAll(List<ProductEntity> products) {
        productRepository.saveAll(products);
    }

    public void pollProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(() ->
                new EntityNotFoundException("Product not found with id: " + productId));

        probeStrategy.probe(product);
        productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Convert {@link Map<LocalDateTime, Map<Integer, Float>>} to {@link List<String>} chart column names.
     * @param prices mapped prices
     * @return Chart column names
     */
    private List<String> probesToChartColumns(Map<LocalDateTime, Map<Integer, Float>> prices) {
        return prices.values()
                .stream()
                .flatMap(wp -> wp.keySet().stream())
                .sorted()
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * Convert {@link Map<LocalDateTime, Map<Integer, Float>>} to chart rows.
     * @param prices mapped prices
     * @return map as timestamp to list of prices which ordered
     * in order to column names. (Dates ordered)
     */
    private Map<String, List<Float>> probesToChartRows(Map<LocalDateTime, Map<Integer, Float>> prices,
                                                       List<String> columns
    ) {
        Map<String, List<Float>> result = new TreeMap<>();

        prices.keySet().forEach(key -> {
            Map<Integer, Float> integerFloatMap = prices.get(key);
            List<Float> priceValues = columns.stream()
                    .map(Integer::parseInt)
                    .map(pf -> ofNullable(integerFloatMap.get(pf)).orElse(0.0F))
                    .collect(Collectors.toList());
            result.put(dateFormatter.format(key), priceValues);
        });
        return result;
    }

    private Map<LocalDateTime, Map<Integer, Float>> getPrices(List<ProductProbeEntity> probes) {
        return probes.stream()
                .collect(Collectors.toMap(ProductProbeEntity::getTimestamp,
                        productProbeEntity -> {
                            Map<Integer, Float> pricesMap = productProbeEntity.getWholesalePrice();
                            pricesMap.put(1, productProbeEntity.getRegularPrice());
                            return pricesMap;
                        }));
    }
}
