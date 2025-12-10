package ro.msg.learning.shop.mapper;

import ro.msg.learning.shop.dto.OrderDetailDto;
import ro.msg.learning.shop.model.OrderDetail;

public class OrderDetailMapper {
    public static OrderDetailDto toDto(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return null;
        }
        return OrderDetailDto.builder()
                .product(ProductMapper.toDto(orderDetail.getOrderProduct().getProduct()))
                .location(LocationMapper.toDto(orderDetail.getLocation()))
                .quantity(orderDetail.getQuantity())
                .build();
    }
}
