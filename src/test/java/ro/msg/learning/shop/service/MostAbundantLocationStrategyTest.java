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
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MostAbundantLocationStrategyTest {

    @Mock
    private StockService stockService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private MostAbundantLocationStrategy mostAbundantLocationStrategy;

    private UUID product1Id;
    private UUID product2Id;
    private Product product1;
    private Product product2;
    private Location location1;
    private Location location2;
    private UUID location1Id;
    private UUID location2Id;
    private Stock stock1;
    private Stock stock2;
    private Order order;

    @BeforeEach
    void setUp() {
        location1Id = UUID.randomUUID();
        location1 = Location.builder()
                .name("Warehouse A")
                .build();
        location1.setId(location1Id);

        product1Id = UUID.randomUUID();
        product1 = Product.builder()
                .name("Product1")
                .description("Desc")
                .build();
        product1.setId(product1Id);
        stock1 = Stock.builder()
                .productLocation(new ProductLocation(product1, location1))
                .quantity(10)
                .build();

        location2Id = UUID.randomUUID();
        location2 = Location.builder()
                .name("Warehouse B")
                .build();
        location2.setId(location2Id);

        product2Id = UUID.randomUUID();
        product2 = Product.builder()
                .name("Product2")
                .description("Desc")
                .build();
        product2.setId(product2Id);
        stock2 = Stock.builder()
                .productLocation(new ProductLocation(product2, location2))
                .quantity(15)
                .build();

        order = new Order();

        when(stockService.findMostAbundantStockForProducts(List.of(product1Id, product2Id).toArray(new UUID[0]))).thenReturn(List.of(stock1,stock2));
        when(productService.readById(product1Id)).thenReturn(product1);
    }

    @Test
    void shouldReturnLocationsWithMostProductsForOrder() {
        when(productService.readById(product2Id)).thenReturn(product2);
        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(product1)
                .build();
        OrderDetail orderDetail1 = OrderDetail.builder()
                .orderProduct(orderProduct1)
                .quantity(5)
                .build();
        OrderProduct orderProduct2 = OrderProduct.builder()
                .product(product2)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .orderProduct(orderProduct2)
                .quantity(5)
                .build();

        order.setOrderDetails(List.of(orderDetail1, orderDetail2));
        order.setCreatedAt(LocalDateTime.now());

        List<OrderDetail> orderDetails = mostAbundantLocationStrategy.selectStockLocations(order);
        assertEquals(2, orderDetails.size());

        orderDetail1 = orderDetails.get(0);
        orderDetail2 = orderDetails.get(1);

        assertEquals(order, orderDetail1.getOrderProduct().getOrder());
        assertEquals(order, orderDetail2.getOrderProduct().getOrder());
        assertEquals(product1, orderDetail1.getOrderProduct().getProduct());
        assertEquals(product2, orderDetail2.getOrderProduct().getProduct());
        assertEquals(location1, orderDetail1.getLocation());
        assertEquals(location2, orderDetail2.getLocation());
        assertEquals(5, orderDetail1.getQuantity());
        assertEquals(5, orderDetail2.getQuantity());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenStockNotFound() {
        OrderProduct orderProduct1 = OrderProduct.builder()
                .product(product1)
                .build();
        OrderDetail orderDetail1 = OrderDetail.builder()
                .orderProduct(orderProduct1)
                .quantity(20)
                .build();
        OrderProduct orderProduct2 = OrderProduct.builder()
                .product(product2)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .orderProduct(orderProduct2)
                .quantity(20)
                .build();

        order.setOrderDetails(List.of(orderDetail1, orderDetail2));
        order.setCreatedAt(LocalDateTime.now());

        Assertions.assertThrows(ShopException.class, () -> mostAbundantLocationStrategy.selectStockLocations(order));
    }
}