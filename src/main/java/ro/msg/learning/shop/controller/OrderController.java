
package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.exception.*;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.service.OrderService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('COSTUMER', 'ADMINISTRATOR')")
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto) {
        try{
            Order order = orderService.createOrder(OrderMapper.toOrder(orderDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(OrderMapper.toDto(order));
        }
        catch(ShopException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") UUID orderId) {
        try {
            orderService.deleteOrder(orderId);
        }
        catch (IllegalArgumentException | ShopException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Order has been deleted");
    }
}
