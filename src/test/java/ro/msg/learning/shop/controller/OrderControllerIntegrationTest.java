package ro.msg.learning.shop.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProductCategoryRepository  productCategoryRepository;

    private String baseUrl;

    private Product product1;

    private Product product2;

    private Location location1;

    private Location location2;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/orders";
        ProductCategory productCategory = productCategoryRepository.save(ProductCategory.builder()
                .name("testCategory")
                .description("categoryDescription")
                .build());


        product1 = productRepository.save(Product.builder()
                .name("Keyboard")
                .description("Mechanical")
                .price(BigDecimal.valueOf(100))
                .category(productCategory)
                .weight(1.0)
                .imageUrl("img")
                .build());
        product2 = productRepository.save(Product.builder()
                .name("Headset")
                .description("Wireless")
                .price(BigDecimal.valueOf(100))
                .category(productCategory)
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
                .productLocation(new ProductLocation(product1, location1))
                .quantity(10)
                .build();

        stockRepository.save(stock1);

        Stock stock2 = Stock.builder()
                .productLocation(new ProductLocation(product2, location2))
                .quantity(15)
                .build();

        stockRepository.save(stock2);
    }

    @Test
    void shouldCreateOrderSuccessfullyAndReturnStatus201() {


        OrderInformation orderInfo = new OrderInformation(
                LocalDateTime.now(),
                new Address("RO", "Cluj", "Cluj", "Str X"),
                List.of(new ProductQuantity(product1.getId(), 10), new ProductQuantity(product2.getId(),10))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderInformation> request = new HttpEntity<>(orderInfo, headers);

        ResponseEntity<OrderDto> response = restTemplate.postForEntity(baseUrl, request, OrderDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getOrderDetails().size());

        assertEquals(location1.getId() ,response.getBody().getOrderDetails().get(0).getLocation().getLocationId());
        assertEquals(product1.getId() ,response.getBody().getOrderDetails().get(0).getProduct().getId());
        assertEquals(location2.getId() ,response.getBody().getOrderDetails().get(1).getLocation().getLocationId());
        assertEquals(product2.getId() ,response.getBody().getOrderDetails().get(1).getProduct().getId());

    }

    @Test
    void shouldThrowStockNotFoundExceptionAndReturnStatus400() {
        OrderInformation orderInfo = new OrderInformation(
                LocalDateTime.now(),
                new Address("RO", "Cluj", "Cluj", "Str X"),
                List.of(new ProductQuantity(product1.getId(), 20), new ProductQuantity(product2.getId(),10))
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<OrderInformation> request = new HttpEntity<>(orderInfo, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Not sufficient stock for " + product1.getName() + " found"));
    }

}
