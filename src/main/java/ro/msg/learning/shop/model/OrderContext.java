package ro.msg.learning.shop.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderContext {
    private Order order;
    private User user;
}
