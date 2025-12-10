package ro.msg.learning.shop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "\"order\"")
public class Order extends  BaseEntity {

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.NEW;

    @OneToMany(mappedBy = "orderProduct.order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

}
