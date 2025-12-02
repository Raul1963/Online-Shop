package ro.msg.learning.shop.mapper;

import ro.msg.learning.shop.dto.RevenueDto;
import ro.msg.learning.shop.model.Revenue;

public class RevenueMapper {

    public static RevenueDto toDto(Revenue revenue) {
        if(revenue==null){
            return null;
        }
        return RevenueDto.builder()
                .revenueId(revenue.getId())
                .locationId(revenue.getLocation().getId())
                .locationName(revenue.getLocation().getName())
                .address(revenue.getLocation().getAddress())
                .salesRevenue(revenue.getSalesRevenue())
                .build();
    }
}
