package ro.msg.learning.shop.mapper;

import ro.msg.learning.shop.dto.ProductDto;
import ro.msg.learning.shop.dto.StockDto;
import ro.msg.learning.shop.model.Stock;

public class StockMapper {

    public static StockDto toDto(Stock stock) {
        if(stock == null) return null;

        return StockDto.builder()
                .location(LocationMapper.toDto(stock.getProductLocation().getLocation()))
                .quantity(stock.getQuantity())
                .build();
    }
}
