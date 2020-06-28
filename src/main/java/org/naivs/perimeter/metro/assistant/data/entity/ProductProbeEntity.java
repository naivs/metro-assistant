package org.naivs.perimeter.metro.assistant.data.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "product_probe")
public class ProductProbeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(insertable = false, updatable = false)
    private Long productId;

    @Column(scale = 2)
    private Float regularPrice;

    @ElementCollection
    @CollectionTable(name = "wholesale_prices")
    @MapKeyColumn(name = "quantity")
    @Column(name = "price", scale = 2)
    private Map<Integer, Float> wholesalePrice = new HashMap<>();

    private Integer leftPct;

    @CreationTimestamp
    private LocalDateTime timestamp;
}
