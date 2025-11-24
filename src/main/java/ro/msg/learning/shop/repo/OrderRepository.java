package ro.msg.learning.shop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.models.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}