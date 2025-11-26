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

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public Product update(Product product) {
        return productRepository.save(product);
    }

    public boolean delete(UUID productId) {
        if(productId == null){
            throw new IllegalArgumentException("Category Id is null");
        }
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found: " + productId);
        }
        productRepository.deleteById(productId);
        return true;
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
