package ro.msg.learning.shop.service;

import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.OrderNotFoundException;
import ro.msg.learning.shop.exception.ProductNotFoundException;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final StockLocationSelectionStrategy stockLocationSelectionStrategy;

    public OrderService(OrderRepository orderRepository, StockRepository stockRepository, StockLocationSelectionStrategy stockLocationSelectionStrategy) {
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.stockLocationSelectionStrategy = stockLocationSelectionStrategy;
    }

    public Order createOrder(OrderInformation orderInformation){
        Order order = Order.builder()
                .createdAt(orderInformation.getCreatedAt())
                .address(orderInformation.getAddress())
                .build();

        List<OrderDetail> orderDetails = stockLocationSelectionStrategy.selectStockLocations(order,orderInformation);
        List<Stock> newStocks = new ArrayList<>();

        for(OrderDetail orderDetail : orderDetails){
            Stock stockToUpdate = stockRepository.findByProductLocation(new ProductLocation(orderDetail.getOrderProduct().getProduct(), orderDetail.getLocation()));

            stockToUpdate.setQuantity(stockToUpdate.getQuantity() - orderDetail.getQuantity());
            newStocks.add(stockToUpdate);
        }

        order.setOrderDetails(orderDetails);

        stockRepository.saveAll(newStocks);

        return orderRepository.save(order);
    }

    public void deleteOrder(UUID orderId){
        if(orderId == null){
            throw new IllegalArgumentException("Order Id is null");
        }
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("Order not found: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }
}
