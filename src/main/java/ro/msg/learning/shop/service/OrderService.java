package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StockService stockService;
    private final StockLocationSelectionStrategy stockLocationSelectionStrategy;
    private final UserService userService;

    public Order createOrder(Order order){

        List<OrderDetail> orderDetails = stockLocationSelectionStrategy.selectStockLocations(order);
        List<Stock> newStocks = new ArrayList<>();

        for(OrderDetail orderDetail : orderDetails){
            Stock stockToUpdate = stockService.findByProductLocation(new ProductLocation(orderDetail.getOrderProduct().getProduct(), orderDetail.getLocation()));

            stockToUpdate.setQuantity(stockToUpdate.getQuantity() - orderDetail.getQuantity());
            newStocks.add(stockToUpdate);
        }

        order.setOrderDetails(orderDetails);

        stockService.saveAll(newStocks);

        User user = userService.getUserByUserName(order.getUser().getUserName());
        order.setUser(user);

        order.setOrderStatus(OrderStatus.NEW);

        return orderRepository.save(order);
    }

    public void deleteOrder(UUID orderId){
        if(orderId == null){
            throw new IllegalArgumentException("Order Id is null");
        }
        if (!orderRepository.existsById(orderId)) {
            throw new ShopException("Order not found: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    public Order updateOrder(Order order){
        if(order.getId() == null){
            throw new ShopException("Order ID is null");
        }
        return orderRepository.save(order);
    }

    public Order getOrder(UUID orderId){
        if(orderId == null){
            throw new ShopException("Order Id is null");
        }

        Optional<Order> order = orderRepository.findById(orderId);

        if(order.isPresent()){
            return order.get();
        }

        throw new ShopException("Order with id: " + orderId + " not found");
    }
}
