package ro.msg.learning.shop.model;

import lombok.*;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInformation {
    private LocalDateTime createdAt;
    private Address address;
    private List<ProductQuantity> products;
}
