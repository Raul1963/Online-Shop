package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.OrderProduct;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderProduct> {

    @Query("""
    SELECT od.location, SUM(od.orderProduct.product.price * od.quantity)
    FROM OrderDetail od
    JOIN od.orderProduct.order o
    WHERE FUNCTION('DATE',o.createdAt) = :day
    GROUP BY od.location
    """)
    List<Object[]> getRevenueByLocation(@Param("day") LocalDate day);
}