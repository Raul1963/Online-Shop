
package ro.msg.learning.shop.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.dto.OrderEventDto;
import ro.msg.learning.shop.exception.*;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderEvent;
import ro.msg.learning.shop.model.OrderStatus;
import ro.msg.learning.shop.service.EmailService;
import ro.msg.learning.shop.service.OrderService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final EmailService emailService;
    private final StateMachineFactory<OrderStatus, OrderEvent> stateMachineFactory;

    @PreAuthorize("hasAnyRole('COSTUMER', 'ADMINISTRATOR')")
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderEventDto orderEventDto) {

        OrderDto orderDto = orderEventDto.getOrder();
        OrderEvent orderEvent = orderEventDto.getOrderEvent();
        try{
            Order order;
            if(orderDto.getOrderId() == null){
                order = orderService.createOrder(OrderMapper.toOrder(orderDto));
                if (!isTransitionAllowed(order.getOrderStatus(), orderEvent)) {
                    return ResponseEntity.badRequest().body("Action: " + orderEvent + " is not allowed for this order status: " + order.getOrderStatus());
                }
                emailService.sendOrderConfirmationMail(order);
            }

            else {
                order = orderService.getOrder(orderDto.getOrderId());

                if (!isTransitionAllowed(order.getOrderStatus(), orderEvent)) {
                    return ResponseEntity.badRequest().body("Action: " + orderEvent + " is not allowed for this order status: " + order.getOrderStatus());
                }
            }

            StateMachine<OrderStatus, OrderEvent> stateMachine = stateMachineFactory.getStateMachine();

            stateMachine.stopReactively().block();
            stateMachine.getStateMachineAccessor()
                    .doWithAllRegions(access -> {
                        access.resetStateMachineReactively(
                                new org.springframework.statemachine.support.DefaultStateMachineContext<>(
                                        order.getOrderStatus(), null, null, null
                                )
                        ).block();
                    });
            stateMachine.startReactively().block();

            stateMachine.sendEvent(
                    MessageBuilder
                            .withPayload(orderEvent)
                            .setHeader("orderId", order.getId())
                            .build()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(OrderMapper.toDto(orderService.getOrder(order.getId())));
        }
        catch(ShopException | MessagingException e){
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

    private boolean isTransitionAllowed(OrderStatus current, OrderEvent event) {
        return switch (current) {
            case NEW -> event == OrderEvent.SAVE || event == OrderEvent.PLACE;
            case SAVED -> event == OrderEvent.PLACE || event == OrderEvent.CANCEL;
            case PLACED -> event == OrderEvent.CANCEL;
            default -> false;
        };
    }
}
