package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.model.ProductLocation;
import ro.msg.learning.shop.model.Stock;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, ProductLocation> {
    Stock findByProductLocation(ProductLocation productLocation);

    @Query(value = """
    SELECT DISTINCT ON (s.product_id) s.*
    FROM "online-shop_schema".stock s
    WHERE s.product_id = ANY(:productIds)
    ORDER BY s.product_id, s.quantity DESC
    """, nativeQuery = true)
    List<Stock> findMostAbundantStockForProducts(@Param("productIds") UUID[] productIds);
}