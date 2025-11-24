package ro.msg.learning.shop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.models.Location;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}