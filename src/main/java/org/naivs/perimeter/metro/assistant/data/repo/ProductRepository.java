package org.naivs.perimeter.metro.assistant.data.repo;

import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
