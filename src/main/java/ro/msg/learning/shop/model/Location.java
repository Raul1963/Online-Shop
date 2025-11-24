package ro.msg.learning.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Location extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Address address;
}
