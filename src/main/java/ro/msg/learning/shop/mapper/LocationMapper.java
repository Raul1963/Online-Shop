package ro.msg.learning.shop.mapper;

import ro.msg.learning.shop.dto.LocationDto;
import ro.msg.learning.shop.model.Location;

public class LocationMapper {

    public static LocationDto toDto(Location location) {
        if(location == null) return null;

        return LocationDto.builder()
                .locationId(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .build();
    }
}
