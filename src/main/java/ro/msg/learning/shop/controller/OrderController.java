
package ro.msg.learning.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.exception.LocationNotFoundException;
import ro.msg.learning.shop.exception.OrderNotFoundException;
import ro.msg.learning.shop.exception.ProductNotFoundException;
import ro.msg.learning.shop.exception.StockNotFoundException;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderInformation;
import ro.msg.learning.shop.service.OrderService;

import java.util.UUID;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyRole('COSTUMER', 'ADMINISTRATOR')")
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderInformation orderInformation) {
        try{
            Order order = orderService.createOrder(orderInformation);
            return ResponseEntity.status(HttpStatus.CREATED).body(OrderMapper.toDto(order));
        }
        catch(LocationNotFoundException | StockNotFoundException | ProductNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") UUID orderId) {
        try {
            orderService.deleteOrder(orderId);
        }
        catch (IllegalArgumentException | OrderNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Order has been deleted");
    }
}
