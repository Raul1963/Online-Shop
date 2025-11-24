package ro.msg.learning.shop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.models.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}