package ro.msg.learning.shop.service;

import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.LocationNotFoundException;
import ro.msg.learning.shop.exception.ProductNotFoundException;
import ro.msg.learning.shop.exception.StockNotFoundException;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("singleLocationStrategy")
public class SingleLocationStrategy implements StockLocationSelectionStrategy {
    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;

    public SingleLocationStrategy(StockRepository stockRepository,
                                  LocationRepository locationRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.locationRepository = locationRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<OrderDetail> selectStockLocations(Order order, OrderInformation orderInformation) {
        Map<UUID, List<Stock>> stockByLocation = stockRepository.findAll().stream().collect(Collectors.groupingBy(stock -> stock.getProductLocation().getLocation().getId()));

        UUID matchingLocation = stockByLocation.entrySet().stream()
                .filter(entry ->
                {
                    List<Stock> stocksAtLocation = entry.getValue();

                    return orderInformation.getProducts().stream()
                            .allMatch(product ->
                                    stocksAtLocation.stream().anyMatch(stock ->
                                            stock.getProductLocation().getProduct().getId().equals(product.getProductId()) &&
                                                    stock.getQuantity() >= product.getQuantity()
                                    )
                            );
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new StockNotFoundException("No location has sufficient stock"));


        Location location = locationRepository.findById(matchingLocation)
                .orElseThrow(() -> new LocationNotFoundException("Location not found"));
        List<OrderDetail> orderDetails = new ArrayList<>();

        for(ProductQuantity productQuantity : orderInformation.getProducts()){
            if(productRepository.findById(productQuantity.getProductId()).isEmpty()){
                throw new ProductNotFoundException("Product with id "+ productQuantity.getProductId() + " not found");
            }
            Product product = productRepository.findById(productQuantity.getProductId()).get();

            OrderDetail orderDetail = OrderDetail.builder()
                    .orderProduct(new OrderProduct(order, product))
                    .location(location)
                    .quantity(productQuantity.getQuantity())
                    .build();

            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }
}
