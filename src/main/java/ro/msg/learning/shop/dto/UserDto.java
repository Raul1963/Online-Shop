package ro.msg.learning.shop.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ro.msg.learning.shop.model.UserRole;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String userName;
    private String emailAddress;
    private UserRole role;
}
