package ro.msg.learning.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.msg.learning.shop.exception.ProductNotFoundException;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProduct() {
        ProductCategory productCategory = ProductCategory.builder()
                .name("testCategory")
                .description("categoryDescription")
                .build();


        productCategory.setId(UUID.randomUUID());

        Product product = Product.builder()
                .name("test")
                .description("description")
                .price(new BigDecimal("250.00"))
                .weight(10.00)
                .category(productCategory)
                .build();

        Product savedProduct = Product.builder()
                .name("test")
                .description("description")
                .price(new BigDecimal("250.00"))
                .weight(10.00)
                .category(productCategory)
                .build();

        savedProduct.setId(UUID.randomUUID());

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.create(product);

        assertEquals(savedProduct, result);
    }

    @Test
    void shouldUpdateProduct() {
        Product updatedProduct = Product.builder()
                .name("test")
                .description("description")
                .price(new BigDecimal("250.00"))
                .weight(10.00)
                .build();

        updatedProduct.setId(UUID.randomUUID());
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.update(updatedProduct);
        assertEquals(updatedProduct, result);

    }

    @Test
    void shouldDeleteProduct() {
        UUID deleteId = UUID.randomUUID();

        when(productRepository.existsById(deleteId)).thenReturn(true);

        boolean isDeleted = productService.delete(deleteId);
        assertTrue(isDeleted);
    }

    @Test
    void shouldThrowExceptionByInvalidDeleteId(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {productService.delete(null);});
        Assertions.assertThrows(IllegalArgumentException.class, () -> {productService.readById(null);});
    }

    @Test
    void shouldThrowExceptionByProductNotFound(){
        Assertions.assertThrows(ProductNotFoundException.class, () -> {productService.delete(UUID.randomUUID());});
        Assertions.assertThrows(ProductNotFoundException.class, () -> {productService.readById(UUID.randomUUID());});
    }

    @Test
    void shouldReturnProductById() {
        Product product = Product.builder()
                .name("test")
                .description("description")
                .price(new BigDecimal("250.00"))
                .weight(10.00)
                .build();
        UUID productId = UUID.randomUUID();
        product.setId(productId);
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(product));

        Product result = productService.readById(productId);

        assertEquals(product, result);
    }

}