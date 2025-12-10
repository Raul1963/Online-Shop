package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Double weight;
    private String imageUrl;
    private UUID categoryId;
    private String categoryName;
    private String categoryDescription;
}
