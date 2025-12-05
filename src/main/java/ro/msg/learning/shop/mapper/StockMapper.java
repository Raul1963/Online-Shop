package ro.msg.learning.shop.mapper;

import ro.msg.learning.shop.dto.StockExportDto;
import ro.msg.learning.shop.model.Stock;

public class StockMapper {

    public static StockExportDto stockToDto(Stock stock) {
        if(stock == null) return null;

        return StockExportDto.builder()
                .locationId(stock.getProductLocation().getLocation().getId())
                .locationName(stock.getProductLocation().getLocation().getName())
                .productId(stock.getProductLocation().getLocation().getId())
                .productName(stock.getProductLocation().getProduct().getName())
                .quantity(stock.getQuantity())
                .build();
    }
}
