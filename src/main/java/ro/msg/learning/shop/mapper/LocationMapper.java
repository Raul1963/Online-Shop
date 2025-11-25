package ro.msg.learning.shop.mapper;

import ro.msg.learning.shop.dto.LocationDto;
import ro.msg.learning.shop.model.Location;

public class LocationMapper {

    public static LocationDto toDto(Location l) {
        if(l == null) return null;

        return LocationDto.builder()
                .locationId(l.getId())
                .name(l.getName())
                .address(l.getAddress())
                .build();
    }
}
