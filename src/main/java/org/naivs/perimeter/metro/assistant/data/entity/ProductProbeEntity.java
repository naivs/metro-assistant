package org.naivs.perimeter.metro.assistant.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

@Data
@Entity
@Table(name = "product_probe")
public class ProductProbeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    @Column(scale = 2, nullable = false)
    private Float regularPrice;

    @ElementCollection
    @CollectionTable(name = "wholesale_prices", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "quantity")
    @Column(name = "price", scale = 2, nullable = false)
    private Map<Integer, Float> wholesalePrice = new TreeMap<>();

    @Column(nullable = false)
    private Integer leftPct;

    @Column(name = "timestamp")
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "ProductProbeEntity{" +
                "id=" + id +
                ", productId=" + product.getId() +
                ", regularPrice=" + regularPrice +
                ", wholesalePrice=" + wholesalePrice +
                ", leftPct=" + leftPct +
                ", timestamp=" + timestamp +
                '}';
    }
}
