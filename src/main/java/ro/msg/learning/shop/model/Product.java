package ro.msg.learning.shop.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Double weight;

    @ManyToOne
    private ProductCategory category;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "productLocation.product", cascade = CascadeType.ALL)
    private List<Stock> stocks;
}
