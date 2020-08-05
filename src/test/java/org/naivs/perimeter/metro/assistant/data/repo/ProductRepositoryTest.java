package org.naivs.perimeter.metro.assistant.data.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.utils.ProductGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProductRepository repository;

    private static ProductEntity product;
    private static ProductEntity persistedProduct;

    @BeforeEach
    private void setup() {
        product = ProductGenerator.generateProducts(1, false).get(0);
        product.setProbes(
                ProductGenerator.generateProbes(80, product, false)
        );
        persistedProduct = entityManager.persistAndFlush(product);
    }

    @Test
    void findByProductIdAndProbesAfter() { // TODO: WRITE TEST ON ProductProbesRepository (delete this)
        List<ProductProbeEntity> persistedAndSortedProbes = persistedProduct.getProbes()
                .stream()
                .sorted(Comparator.comparing(ProductProbeEntity::getTimestamp))
                .collect(Collectors.toList());

        ProductEntity foundProduct = repository.findById(product.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));

        List<ProductProbeEntity> foundProbes = foundProduct.getProbes();
        assertNotNull(foundProduct);
        assertEquals(persistedProduct, foundProduct);
        assertEquals(persistedAndSortedProbes, foundProbes);
    }
}