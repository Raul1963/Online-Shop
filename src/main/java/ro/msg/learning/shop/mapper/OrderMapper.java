package ro.msg.learning.shop.mapper;

import ro.msg.learning.shop.dto.OrderDetailDto;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.model.Order;

public class OrderMapper {

    public static OrderDto toDto(Order order) {
        if(order == null)
            return null;
        return OrderDto.builder()
                .orderId(order.getId())
                .orderDate(order.getCreatedAt())
                .address(order.getAddress())
                .orderDetails(order.getOrderDetails().stream().map(OrderDetailMapper::toDto).toList())
                .build();
    }
}
