package org.naivs.perimeter.metro.assistant.service;

import lombok.RequiredArgsConstructor;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.naivs.perimeter.metro.assistant.data.repo.ProductRepository;
import org.naivs.perimeter.metro.assistant.model.MetroProduct;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final MetroClient metroClient;
    private final ProductRepository productRepository;

    public ProductEntity addProduct(String productUrl) {
        ProductEntity product = new ProductEntity();
        product.setUrl(productUrl);

        probe(product);

        return productRepository.saveAndFlush(product);
    }

    public List<ProductEntity> getProducts() {
        return productRepository.findAll();
    }

    public void probe(ProductEntity product) {
        MetroProduct metroProduct = metroClient.getItem(
                product.getUrl()
        );

        product.setMetroId(metroProduct.getId());
        product.setName(metroProduct.getName());
        product.setPack(metroProduct.getMe());
        product.getProbes().add(extractProbe(metroProduct));
    }

    public void saveOrUpdateAll(List<ProductEntity> products) {
        productRepository.saveAll(products);
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
