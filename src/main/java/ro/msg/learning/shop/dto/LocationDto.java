package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ro.msg.learning.shop.model.Address;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LocationDto {
    private UUID locationId;
    private String name;
    private Address address;
}
