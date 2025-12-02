package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.ShopException;
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
@RequiredArgsConstructor
public class SingleLocationStrategy implements StockLocationSelectionStrategy {
    private final StockService stockService;
    private final LocationRepository locationRepository;
    private final ProductService productService;


    @Override
    public List<OrderDetail> selectStockLocations(Order order) {
        Map<UUID, List<Stock>> stockByLocation = stockService.findAll().stream().collect(Collectors.groupingBy(stock -> stock.getProductLocation().getLocation().getId()));

        UUID matchingLocation = stockByLocation.entrySet().stream()
                .filter(entry ->
                {
                    List<Stock> stocksAtLocation = entry.getValue();

                    return order.getOrderDetails().stream()
                            .allMatch(orderDetail ->
                                    stocksAtLocation.stream().anyMatch(stock ->
                                            stock.getProductLocation().getProduct().getId().equals(orderDetail.getOrderProduct().getProduct().getId()) &&
                                                    stock.getQuantity() >= orderDetail.getQuantity()
                                    )
                            );
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new ShopException("No location has sufficient stock"));


        Location location = locationRepository.findById(matchingLocation)
                .orElseThrow(() -> new ShopException("Location not found"));
        List<OrderDetail> orderDetails = new ArrayList<>();

        for(OrderDetail orderDetail : order.getOrderDetails()){
            Product product = productService.readById(orderDetail.getOrderProduct().getProduct().getId());

            orderDetail.setLocation(location);
            orderDetail.setOrderProduct(new OrderProduct(order,product));

            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }
}
