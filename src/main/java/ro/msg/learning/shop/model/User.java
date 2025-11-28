package ro.msg.learning.shop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,  unique = true)
    private String emailAddress;

    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;


    public User(String firstName, String lastName, String userName, String password, String emailAddress, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.emailAddress = emailAddress;
        this.role = role;
    }


}
