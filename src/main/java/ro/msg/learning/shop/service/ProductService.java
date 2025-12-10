package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.repository.ProductCategoryRepository;
import ro.msg.learning.shop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductCategoryService productCategoryService;

    public Product create(Product product) {
        ProductCategory productCategory = productCategoryService.findById(product.getCategory().getId());
        product.setCategory(productCategory);

        return productRepository.save(product);
    }

    public Product update(Product product) {
        ProductCategory productCategory = productCategoryService.findById(product.getCategory().getId());
        productCategory.setName(product.getCategory().getName());
        productCategory.setDescription(product.getCategory().getDescription());
        productCategoryService.save(productCategory);
        product.setCategory(productCategory);
        return productRepository.save(product);
    }

    public boolean delete(UUID productId) {
        if(productId == null){
            throw new IllegalArgumentException("Category Id is null");
        }
        if (!productRepository.existsById(productId)) {
            throw new ShopException("Product not found: " + productId);
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
        throw new ShopException("Product with id:" + productId + " not found");
    }

    public List<Product> readAll() {
        return productRepository.findAll();
    }
}
