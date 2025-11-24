package ro.msg.learning.shop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.models.ProductCategory;

import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
}