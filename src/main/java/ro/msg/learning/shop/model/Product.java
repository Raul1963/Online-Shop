package ro.msg.learning.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Double weight;

    @ManyToOne(cascade = CascadeType.ALL)
    private ProductCategory category;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "productLocation.product", cascade = CascadeType.ALL)
    private List<Stock> stocks;
}
