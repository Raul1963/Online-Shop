package ro.msg.learning.shop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.models.OrderDetail;
import ro.msg.learning.shop.models.OrderProduct;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderProduct> {
}