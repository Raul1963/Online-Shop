package ro.msg.learning.shop.service;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
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

    public OrderService(OrderRepository orderRepository, StockRepository stockRepository, LocationRepository locationRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.locationRepository = locationRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(OrderInformation orderInformation){
        Order order = Order.builder()
                .createdAt(orderInformation.getCreatedAt())
                .address(orderInformation.getAddress())
                .build();

        Map<UUID, List<Stock>> stockByLocation = stockRepository.findAll().stream().collect(Collectors.groupingBy(stock -> stock.getProductLocation().getLocation().getId()));

        List<UUID> matchingLocations = stockByLocation.entrySet().stream()
                .filter(entry ->
                {
                    List<Stock> stocksAtLocation = entry.getValue();

                    return orderInformation.getProducts().stream()
                            .allMatch(product ->
                                    stocksAtLocation.stream().anyMatch(stock ->
                                            stock.getProductLocation().getProduct().getId().equals(product.getFirst()) &&
                                            stock.getQuantity() >= product.getSecond()
                                    )
                            );
                })
                .map(Map.Entry::getKey)
                .toList();

        if(matchingLocations.isEmpty()){
            throw new StockNotFoundException("Stock not found");
        }
        Location location = locationRepository.findById(matchingLocations.get(0))
                .orElseThrow(() -> new LocationNotFoundException("Location not found"));

        List<Stock> newStocks = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();

        for(Pair<UUID, Integer> products : orderInformation.getProducts()){
            Product product = productRepository.findById(products.getFirst()).get();
            Stock stockToUpdate = stockRepository.findByProductLocation(new ProductLocation(product, location));

            stockToUpdate.setQuantity(stockToUpdate.getQuantity() - products.getSecond());
            newStocks.add(stockToUpdate);

            OrderDetail orderDetail = OrderDetail.builder()
                    .orderProduct(new OrderProduct(order, product))
                    .location(location)
                    .quantity(products.getSecond())
                    .build();

            orderDetails.add(orderDetail);
        }

        order.setOrderDetails(orderDetails);

        stockRepository.saveAll(newStocks);

        return orderRepository.save(order);
    }
}
