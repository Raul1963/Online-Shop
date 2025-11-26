package ro.msg.learning.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.learning.shop.exception.StockNotFoundException;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SingleLocationStrategyTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SingleLocationStrategy singleLocationStrategy;

    private UUID locationId;
    private Location location;
    private Product product;
    private UUID productId;
    private Stock stock;
    private Order order;

    @BeforeEach
    void setUp() {
        locationId = UUID.randomUUID();
        location = Location.builder()
                .name("Warehouse A")
                .build();
        location.setId(locationId);

        productId = UUID.randomUUID();
        product = Product.builder()
                .name("Product1")
                .description("Desc")
                .build();
        product.setId(productId);
        stock = Stock.builder()
                .productLocation(new ProductLocation(product, location))
                .quantity(10)
                .build();

        when(stockRepository.findAll()).thenReturn(List.of(stock));

        order = new Order();
    }

    @Test
    void returnsLocationWithAllProducts() {
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductQuantity orderProduct = new ProductQuantity(productId, 5);

        OrderInformation orderInfo = new OrderInformation();
        orderInfo.setProducts(List.of(orderProduct));
        orderInfo.setCreatedAt(LocalDateTime.now());

        List<OrderDetail> orderDetails = singleLocationStrategy.selectStockLocations(order, orderInfo);
        assertEquals(1, orderDetails.size());

        OrderDetail orderDetail = orderDetails.get(0);

        assertEquals(order, orderDetail.getOrderProduct().getOrder());
        assertEquals(product, orderDetail.getOrderProduct().getProduct());
        assertEquals(location, orderDetail.getLocation());
        assertEquals(5, orderDetail.getQuantity());

    }

    @Test
    void shouldThrowStockNotFoundException() {
        ProductQuantity orderProduct = new ProductQuantity(productId, 11);

        OrderInformation orderInfo = new OrderInformation();
        orderInfo.setProducts(List.of(orderProduct));
        orderInfo.setCreatedAt(LocalDateTime.now());

        Assertions.assertThrows(StockNotFoundException.class, () -> singleLocationStrategy.selectStockLocations(order, orderInfo));
    }
}