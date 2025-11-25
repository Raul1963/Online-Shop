package ro.msg.learning.shop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {
    private ProductDto product;
    private LocationDto location;
    private Integer quantity;
}
