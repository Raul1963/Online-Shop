package ro.msg.learning.shop.service;

import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.ProductNotFoundException;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void create(Product product) {
        productRepository.save(product);
    }

    public void update(Product product) {
        productRepository.save(product);
    }

    public void delete(UUID productId) {
        if(productId == null){
            throw new IllegalArgumentException("Category Id is null");
        }
        productRepository.deleteById(productId);
    }

    public Product readById(UUID productId) {
        if(productId == null){
            throw new IllegalArgumentException("Category Id is null");
        }
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get();
        }
        throw new ProductNotFoundException("Product with id:" + productId + " not found");
    }

    public List<Product> readAll() {
        return productRepository.findAll();
    }
}
