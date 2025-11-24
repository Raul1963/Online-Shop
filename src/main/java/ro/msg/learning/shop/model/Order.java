package ro.msg.learning.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Order extends  BaseEntity {

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "orderProduct.order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

}
