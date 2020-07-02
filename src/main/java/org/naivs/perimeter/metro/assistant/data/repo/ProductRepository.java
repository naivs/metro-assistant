package org.naivs.perimeter.metro.assistant.data.repo;

import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("select p from ProductEntity p " +
            "inner join ProductProbeEntity pp on p.id = pp.productId " +
            "where p.id = :productId and pp.timestamp > :date")
    Optional<ProductEntity> findByProductIdAndProbesAfter(@Param("productId") Long productId,
                                                          @Param("date") LocalDateTime date);
}
