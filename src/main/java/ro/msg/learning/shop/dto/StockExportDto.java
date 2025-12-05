package ro.msg.learning.shop.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockExportDto {
    private UUID locationId;
    private String locationName;
    private UUID productId;
    private String productName;
    private Integer quantity;
}
