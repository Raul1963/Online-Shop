package ro.msg.learning.shop.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Stock {

    @EmbeddedId
    private ProductLocation productLocation;

    @Column(nullable = false)
    private Integer quantity;
}
