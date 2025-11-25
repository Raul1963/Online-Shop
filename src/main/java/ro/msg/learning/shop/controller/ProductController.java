package ro.msg.learning.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dto.ProductCreateDto;
import ro.msg.learning.shop.dto.ProductDto;
import ro.msg.learning.shop.exception.ProductCategoryNotFoundException;
import ro.msg.learning.shop.exception.ProductNotFoundException;
import ro.msg.learning.shop.mapper.ProductMapper;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.service.ProductCategoryService;
import ro.msg.learning.shop.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {

    private final ProductService productService;

    private final ProductCategoryService productCategoryService;

    public ProductController(ProductService productService, ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(){
        List<Product> products = productService.readAll();
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Products not found");
        }
        return ResponseEntity.ok(products.stream().map(ProductMapper::toDto).toList()) ;
    }

    @GetMapping("products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id){
        try {
            Product product = productService.readById(id);
            return ResponseEntity.ok(ProductMapper.toDto(product));
        }
        catch (ProductNotFoundException | IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateDto productCreateDto){
        Product product = ProductMapper.toCreateProduct(productCreateDto);
        ProductCategory productCategory;
        try{
            productCategory = productCategoryService.findById(productCreateDto.getCategoryId());
        }
        catch (ProductCategoryNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        product.setCategory(productCategory);
        productService.create(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody ProductDto productDto){
        if(productService.readById(id) == null){
            return ResponseEntity.notFound().build();
        }

        Product product = ProductMapper.toProduct(productDto);
        ProductCategory productCategory;
        try{
            productCategory = productCategoryService.findById(productDto.getCategoryId());
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
        catch (ProductNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        productCategory.setName(productDto.getCategoryName());
        productCategory.setDescription(productDto.getCategoryDescription());

        product.setCategory(productCategory);
        productService.update(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id){
        if(productService.readById(id) == null){
            return ResponseEntity.notFound().build();
        }
        try {
            productService.delete(id);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product has been deleted");
    }
}
