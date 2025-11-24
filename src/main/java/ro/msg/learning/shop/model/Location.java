package ro.msg.learning.shop.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Location extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Address address;
}
