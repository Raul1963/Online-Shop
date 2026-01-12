package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.mapper.UserMapper;
import ro.msg.learning.shop.model.User;
import ro.msg.learning.shop.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('COSTUMER', 'ADMINISTRATOR')")
    @GetMapping("/users/{userName}")
    public ResponseEntity<?> getUser(@PathVariable String userName) {
        try{
            User user = userService.findUserByUserName(userName);
            return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toDto(user));
        }
        catch(ShopException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
