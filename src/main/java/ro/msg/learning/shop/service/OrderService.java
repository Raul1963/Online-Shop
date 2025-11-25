package ro.msg.learning.shop.service;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.config.StockLocationSelectionStrategy;
import ro.msg.learning.shop.exception.LocationNotFoundException;
import ro.msg.learning.shop.exception.StockNotFoundException;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;
    private final StockLocationSelectionStrategy stockLocationSelectionStrategy;

    public OrderService(OrderRepository orderRepository, StockRepository stockRepository, LocationRepository locationRepository, ProductRepository productRepository, StockLocationSelectionStrategy stockLocationSelectionStrategy) {
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.locationRepository = locationRepository;
        this.productRepository = productRepository;
        this.stockLocationSelectionStrategy = stockLocationSelectionStrategy;
    }

    public Order createOrder(OrderInformation orderInformation){
        Order order = Order.builder()
                .createdAt(orderInformation.getCreatedAt())
                .address(orderInformation.getAddress())
                .build();

//        Map<UUID, List<Stock>> stockByLocation = stockRepository.findAll().stream().collect(Collectors.groupingBy(stock -> stock.getProductLocation().getLocation().getId()));
//
//        List<UUID> matchingLocations = stockByLocation.entrySet().stream()
//                .filter(entry ->
//                {
//                    List<Stock> stocksAtLocation = entry.getValue();
//
//                    return orderInformation.getProducts().stream()
//                            .allMatch(product ->
//                                    stocksAtLocation.stream().anyMatch(stock ->
//                                            stock.getProductLocation().getProduct().getId().equals(product.getProductId()) &&
//                                            stock.getQuantity() >= product.getQuantity()
//                                    )
//                            );
//                })
//                .map(Map.Entry::getKey)
//                .toList();
//
//        if(matchingLocations.isEmpty()){
//            throw new StockNotFoundException("Stock not found");
//        }
//        Location location = locationRepository.findById(matchingLocations.get(0))
//                .orElseThrow(() -> new LocationNotFoundException("Location not found"));
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
}
