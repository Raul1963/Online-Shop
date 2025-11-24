package ro.msg.learning.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderDetail implements Serializable {

    @EmbeddedId
    private OrderProduct orderProduct;

    @ManyToOne
    private Location location;

    @Column(nullable = false)
    private Integer quantity;
}
