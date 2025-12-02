package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.model.ProductCategory;
import ro.msg.learning.shop.repository.ProductCategoryRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategory findById(UUID id) {
        if(id == null){
            throw new IllegalArgumentException("Category Id is null");
        }
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(id);
        if(productCategory.isPresent()) {
            return productCategory.get();
        }
        throw new ShopException("Product category with id:" + id + " not found");
    }

    public ProductCategory save(ProductCategory productCategory) {
        if(productCategory == null){
            throw new IllegalArgumentException("Category Id is null");
        }
        return productCategoryRepository.save(productCategory);
    }
}
