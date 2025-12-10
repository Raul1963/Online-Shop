package ro.msg.learning.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class OrderIntegrationTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private OrderService orderService;

    private Product product1;

    private Product product2;

    private Location location1;

    private Location location2;

    @BeforeEach
    void setUp() {
       product1 = productRepository.save(Product.builder()
                .name("Keyboard")
                .description("Mechanical")
                .price(BigDecimal.valueOf(100))
                .weight(1.0)
                .imageUrl("img")
                .build());
       product2 = productRepository.save(Product.builder()
                .name("Headset")
                .description("Wireless")
                .price(BigDecimal.valueOf(100))
                .weight(1.0)
                .imageUrl("img")
                .build());

        location1 = locationRepository.save(Location.builder()
                .name("Warehouse 1")
                .address(new Address("romania", "Cluj-Napoca", "Cluj", "Dorobanti"))
                .build());
        location2 = locationRepository.save(Location.builder()
                .name("Warehouse 2")
                .address(new Address("romania", "Cluj-Napoca", "Cluj", "Dorobanti"))
                .build());

        Stock stock1 = Stock.builder()
                .productLocation(new ProductLocation(product1,location1))
                .quantity(10)
                .build();

        stockRepository.save(stock1);

        Stock stock2 = Stock.builder()
                .productLocation(new ProductLocation(product2,location2))
                .quantity(15)
                .build();

        stockRepository.save(stock2);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(product1)
                .build();

        OrderProduct orderProduct2 = OrderProduct.builder()
                .product(product2)
                .build();

        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .address(new Address("RO", "Cluj", "Cluj", "Str X"))
                .orderDetails(List.of(OrderDetail.builder().orderProduct(orderProduct1).quantity(10).build(), OrderDetail.builder().orderProduct(orderProduct2).quantity(10).build()))
                .build();

        order = orderService.createOrder(order);

        assertNotNull(order);
        assertNotNull(order.getId());
        assertEquals(2, order.getOrderDetails().size());
        assertEquals(location1.getId() ,order.getOrderDetails().get(0).getLocation().getId());
        assertEquals(product1.getId() ,order.getOrderDetails().get(0).getOrderProduct().getProduct().getId());
        assertEquals(location2.getId() ,order.getOrderDetails().get(1).getLocation().getId());
        assertEquals(product2.getId() ,order.getOrderDetails().get(1).getOrderProduct().getProduct().getId());

    }

    @Test
    void shouldThrowStockNotFoundException() {
        Stock stock2 = Stock.builder()
                .productLocation(new ProductLocation(product2,location2))
                .quantity(15)
                .build();

        stockRepository.save(stock2);

        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(product1)
                .build();

        OrderProduct orderProduct2 = OrderProduct.builder()
                .product(product2)
                .build();

        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .address(new Address("RO", "Cluj", "Cluj", "Str X"))
                .orderDetails(List.of(OrderDetail.builder().orderProduct(orderProduct1).quantity(20).build(), OrderDetail.builder().orderProduct(orderProduct2).quantity(10).build()))
                .build();

        Assertions.assertThrows(ShopException.class, () -> orderService.createOrder(order));
    }
}