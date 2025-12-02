package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dto.ProductCreateDto;
import ro.msg.learning.shop.dto.ProductDto;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.mapper.ProductMapper;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.service.ProductCategoryService;
import ro.msg.learning.shop.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductCategoryService productCategoryService;

    @PreAuthorize("hasAnyRole('COSTUMER', 'ADMINISTRATOR')")
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(){
        List<Product> products = productService.readAll();
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Products not found");
        }
        return ResponseEntity.ok(products.stream().map(ProductMapper::toDto).toList()) ;
    }

    @PreAuthorize("hasAnyRole('COSTUMER', 'ADMINISTRATOR')")
    @GetMapping("products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id){
        try {
            Product product = productService.readById(id);
            return ResponseEntity.ok(ProductMapper.toDto(product));
        }
        catch (ShopException | IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateDto productCreateDto){
        Product product = ProductMapper.toProduct(productCreateDto);
        try {
            product = productService.create(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toDto(product));
        }
        catch (ShopException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody ProductDto productDto){
        Product product = ProductMapper.toProduct(productDto);
        try {
            product = productService.update(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toDto(product));
        }
        catch (ShopException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id){
        try {
            boolean deleted = productService.delete(id);
            if(!deleted){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found");
            }
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (ShopException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product has been deleted");
    }
}
