package ro.msg.learning.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SingleLocationStrategyTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private StockService stockService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private SingleLocationStrategy singleLocationStrategy;

    private UUID locationId;
    private Location location;
    private Product product;
    private UUID productId;

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
        Stock stock = Stock.builder()
                .productLocation(new ProductLocation(product, location))
                .quantity(10)
                .build();

        when(stockService.findAll()).thenReturn(List.of(stock));

    }

    @Test
    void returnsLocationWithAllProducts() {
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(productService.readById(productId)).thenReturn(product);

        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(product)
                .build();


        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .address(new Address("RO", "Cluj", "Cluj", "Str X"))
                .orderDetails(List.of(OrderDetail.builder().orderProduct(orderProduct1).quantity(5).build()))
                .build();


        List<OrderDetail> orderDetails = singleLocationStrategy.selectStockLocations(order);
        assertEquals(1, orderDetails.size());

        OrderDetail orderDetail = orderDetails.get(0);

        assertEquals(order, orderDetail.getOrderProduct().getOrder());
        assertEquals(product, orderDetail.getOrderProduct().getProduct());
        assertEquals(location, orderDetail.getLocation());
        assertEquals(5, orderDetail.getQuantity());

    }

    @Test
    void shouldThrowStockNotFoundException() {
        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(product)
                .build();


        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .address(new Address("RO", "Cluj", "Cluj", "Str X"))
                .orderDetails(List.of(OrderDetail.builder().orderProduct(orderProduct1).quantity(11).build()))
                .build();

        Assertions.assertThrows(ShopException.class, () -> singleLocationStrategy.selectStockLocations(order));
    }
}