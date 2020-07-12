package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.enums.HistoryRange;
import org.naivs.perimeter.metro.assistant.data.model.PriceHistoryModel;
import org.naivs.perimeter.metro.assistant.data.repo.ProductRepository;
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
        ProductEntity product = productRepository
                .findByProductIdAndProbesAfter(productId, HistoryRange.MONTH.get())
                .orElseThrow(() ->
                        new RuntimeException("Product not found with \"id\"=" + productId));

        Map<LocalDateTime, Map<Integer, Float>> prices = product.getProbes().stream()
                .collect(Collectors.toMap(ProductProbeEntity::getTimestamp,
                        productProbeEntity -> {
                            Map<Integer, Float> pricesMap = productProbeEntity.getWholesalePrice();
                            pricesMap.put(1, productProbeEntity.getRegularPrice());
                            return pricesMap;
                        }));

        PriceHistoryModel priceHistory = new PriceHistoryModel();
//        priceHistory.getColumns().add("reg");
        // find prices date with max offers (needs to be sorted)
        Set<Integer> priceFields = prices.entrySet().stream()
                .max(Comparator.comparingInt(ws -> ws.getValue().size()))
                .map(e -> new TreeSet<>(e.getValue().keySet()))
                .orElse(new TreeSet<>());

        priceFields.forEach(pf -> priceHistory.getColumns().add(String.valueOf(pf)));

        Map<String, List<Float>> rows = priceHistory.getRows();
        prices.keySet()
                .forEach(key -> {
                    List<Float> priceValues = new ArrayList<>();
                    Map<Integer, Float> integerFloatMap = prices.get(key);
                    priceFields.forEach(pf -> priceValues.add(
                            ofNullable(integerFloatMap.get(pf)).orElse(0.0F)
                    ));
                    rows.put(dateFormatter.format(key), priceValues);
                });
        return priceHistory;
    }

    public Map<String, Float> getPriceHistory(Long productId, HistoryRange range) {
        LocalDateTime queryDate = range.get();
        return productRepository
                .findByProductIdAndProbesAfter(productId, queryDate)
        .map(product -> product.getProbes().stream()
                .collect(Collectors.toMap(
                        productProbeEntity -> dateFormatter.format(productProbeEntity.getTimestamp()),
                        ProductProbeEntity::getRegularPrice))).orElse(new HashMap<>());
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
