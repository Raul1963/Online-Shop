package ro.msg.learning.shop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.models.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}