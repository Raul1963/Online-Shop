package ro.msg.learning.shop.mapper;

import ro.msg.learning.shop.dto.LoginDto;
import ro.msg.learning.shop.dto.RegisterDto;
import ro.msg.learning.shop.model.User;
import ro.msg.learning.shop.model.UserRole;

public class UserMapper {

    public static User toUser(RegisterDto registerDto) {
        return User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .userName(registerDto.getUserName())
                .password(registerDto.getPassword())
                .emailAddress(registerDto.getEmail())
                .role(UserRole.valueOf(registerDto.getRole().trim().toUpperCase()))
                .build();
    }

    public static User toUser(LoginDto loginDto) {
        if(loginDto == null) return null;

        return User.builder()
                .userName(loginDto.getUsername())
                .password(loginDto.getPassword())
                .build();
    }
}
