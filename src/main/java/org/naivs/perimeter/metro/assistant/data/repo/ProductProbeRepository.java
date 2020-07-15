package org.naivs.perimeter.metro.assistant.data.repo;

import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductProbeRepository extends JpaRepository<ProductProbeEntity, Long> {

    @Query("select pp from ProductProbeEntity pp " +
            "inner join ProductEntity p on pp.product.id = p.id " +
            "where p.id = :productId and pp.timestamp > :date")
    List<ProductProbeEntity> findByProductIdAndProbesAfter(@Param("productId") Long productId,
                                                           @Param("date") LocalDateTime date);
}
