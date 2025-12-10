package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.math.BigDecimal;

@RestController
@RequestMapping("/test")
@Profile("test")
@RequiredArgsConstructor
public class SetupDatabaseController {
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final OrderRepository orderRepository;

    @PostMapping("/clear")
    public void clearDatabase() {
        orderRepository.deleteAll();
        stockRepository.deleteAll();
        productRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @PostMapping("/populate")
    public void populateDatabase() {
        clearDatabase();
        Product p1 = productRepository.save(Product.builder()
                .name("Keyboard")
                .description("Mechanical")
                .price(BigDecimal.valueOf(100))
                .weight(1.0)
                .imageUrl("img")
                .build());

        Product p2 = productRepository.save(Product.builder()
                .name("Headset")
                .description("Wireless")
                .price(BigDecimal.valueOf(150))
                .weight(0.5)
                .imageUrl("img")
                .build());

        Location loc1 = locationRepository.save(Location.builder()
                .name("Warehouse 1")
                .address(new Address("RO", "Cluj", "Cluj", "Str X"))
                .build());

        Location loc2 = locationRepository.save(Location.builder()
                .name("Warehouse 2")
                .address(new Address("RO", "Cluj", "Cluj", "Str Y"))
                .build());

        stockRepository.save(Stock.builder()
                .productLocation(new ProductLocation(p1, loc1))
                .quantity(10)
                .build());

        stockRepository.save(Stock.builder()
                .productLocation(new ProductLocation(p2, loc2))
                .quantity(5)
                .build());
    }
}
