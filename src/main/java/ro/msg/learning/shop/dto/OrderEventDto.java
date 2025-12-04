package ro.msg.learning.shop.dto;

import lombok.*;
import ro.msg.learning.shop.model.OrderEvent;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventDto {
    private OrderDto order;
    private OrderEvent orderEvent;
}
