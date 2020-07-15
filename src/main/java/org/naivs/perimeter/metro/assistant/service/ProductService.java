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

        Map<LocalDateTime, Map<Integer, Float>> prices = product.getProbes().stream()
                .collect(Collectors.toMap(ProductProbeEntity::getTimestamp,
                        productProbeEntity -> {
                            Map<Integer, Float> pricesMap = productProbeEntity.getWholesalePrice();
                            pricesMap.put(1, productProbeEntity.getRegularPrice());
                            return pricesMap;
                        }));

        PriceHistoryModel priceHistory = new PriceHistoryModel();
        Set<Integer> priceFields = prices.values()
                .stream()
                .flatMap(wp -> wp.keySet().stream())
                .sorted()
                .collect(Collectors.toCollection(TreeSet::new));

        priceFields.forEach(pf -> priceHistory.getColumns().add(String.valueOf(pf)));

        prices.keySet()
                .forEach(key -> {
                    List<Float> priceValues = new ArrayList<>();
                    Map<Integer, Float> integerFloatMap = prices.get(key);
                    priceFields.forEach(pf -> priceValues.add(
                            ofNullable(integerFloatMap.get(pf)).orElse(0.0F)
                    ));
                    priceHistory.getRows().put(dateFormatter.format(key), priceValues);
                });
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
}
