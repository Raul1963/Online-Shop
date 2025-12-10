package ro.msg.learning.shop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Revenue extends BaseEntity {

    @ManyToOne
    private Location location;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Double salesRevenue;
}
