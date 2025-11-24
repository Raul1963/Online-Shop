package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.model.ProductLocation;
import ro.msg.learning.shop.model.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, ProductLocation> {
}