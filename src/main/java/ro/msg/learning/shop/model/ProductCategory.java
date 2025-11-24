package ro.msg.learning.shop.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class ProductCategory extends BaseEntity{


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

}
