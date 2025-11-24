package ro.msg.learning.shop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.models.ProductLocation;
import ro.msg.learning.shop.models.Stock;

public interface StockRepository extends JpaRepository<Stock, ProductLocation> {
}