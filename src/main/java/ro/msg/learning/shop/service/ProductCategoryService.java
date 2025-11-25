package ro.msg.learning.shop.service;

import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.ProductCategoryNotFoundException;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.repository.ProductCategoryRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public ProductCategory findById(UUID id) {
        if(id == null){
            throw new IllegalArgumentException("Category Id is null");
        }
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(id);
        if(productCategory.isPresent()) {
            return productCategory.get();
        }
        throw new ProductCategoryNotFoundException("Product category with id:" + id + " not found");
    }
}
