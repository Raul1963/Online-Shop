package ro.msg.learning.shop.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class Address implements Serializable {
    private String country;
    private String city;
    private String county;
    private String streetAddress;
}
