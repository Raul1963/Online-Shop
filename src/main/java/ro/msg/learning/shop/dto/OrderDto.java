package ro.msg.learning.shop.dto;

import lombok.*;
import ro.msg.learning.shop.model.Address;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private UUID orderId;
    private LocalDateTime orderDate;
    private String userName;
    private Address address;
    private List<OrderDetailDto> orderDetails;
}
