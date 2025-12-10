package ro.msg.learning.shop.util;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderEvent;
import ro.msg.learning.shop.model.OrderStatus;
import ro.msg.learning.shop.service.OrderService;

import java.util.UUID;

@Component
@WithStateMachine
@RequiredArgsConstructor
public class OrderStateChangeHandler {

    private final OrderService orderService;

    @OnTransition(source = "NEW", target = "SAVED")
    public void newToSaved(StateContext<OrderStatus, OrderEvent> context) {
        UUID orderId = (UUID) context.getMessageHeader("orderId");

        updateStatus(orderId, OrderStatus.SAVED);
    }

    @OnTransition(source = "NEW", target = "PLACED")
    public void newToPlaced(StateContext<OrderStatus, OrderEvent> context) {
        UUID orderId = (UUID) context.getMessageHeader("orderId");

        updateStatus(orderId, OrderStatus.PLACED);
    }

    @OnTransition(source = "SAVED", target = "CANCELED")
    public void savedToCanceled(StateContext<OrderStatus, OrderEvent> context) {
        UUID orderId = (UUID) context.getMessageHeader("orderId");

        updateStatus(orderId, OrderStatus.CANCELED);
    }

    @OnTransition(source = "SAVED", target = "PLACED")
    public void savedToPlaced(StateContext<OrderStatus, OrderEvent> context) {
        UUID orderId = (UUID) context.getMessageHeader("orderId");

        updateStatus(orderId, OrderStatus.PLACED);
    }

    @OnTransition(source = "PLACED", target = "CANCELED")
    public void placedToCanceled(StateContext<OrderStatus, OrderEvent> context) {
        UUID orderId = (UUID) context.getMessageHeader("orderId");
        updateStatus(orderId, OrderStatus.CANCELED);
    }


    public void updateStatus(UUID orderId, OrderStatus orderStatus) {
        Order order = orderService.getOrder(orderId);

        order.setOrderStatus(orderStatus);

        orderService.updateOrder(order);
    }
}
