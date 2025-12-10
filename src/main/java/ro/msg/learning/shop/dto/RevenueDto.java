package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ro.msg.learning.shop.model.Address;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RevenueDto {

    private UUID revenueId;

    private UUID locationId;

    private String locationName;

    private Address address;

    private LocalDate date;

    private Double salesRevenue;

}
