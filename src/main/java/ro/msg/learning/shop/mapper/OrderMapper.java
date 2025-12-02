package ro.msg.learning.shop.mapper;

import ro.msg.learning.shop.dto.OrderDetailDto;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.model.*;

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

    public static Order toOrder(OrderDto orderDto) {
        if(orderDto == null)
            return null;
        return Order.builder()
                .createdAt(orderDto.getOrderDate())
                .address(orderDto.getAddress())
                .orderDetails(orderDto.getOrderDetails().stream().map(orderDetail ->
                        {
                            Product product = new Product();
                            product.setId(orderDetail.getProduct().getId());

                            OrderProduct orderProduct = OrderProduct.builder()
                                                                    .product(product)
                                                                    .build();
                            return OrderDetail.builder()
                                    .quantity(orderDetail.getQuantity())
                                    .orderProduct(orderProduct)
                                    .build();
                        })
                        .toList())
                .build();
    }
}
