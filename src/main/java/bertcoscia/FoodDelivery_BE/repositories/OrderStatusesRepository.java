package bertcoscia.FoodDelivery_BE.repositories;

import bertcoscia.FoodDelivery_BE.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderStatusesRepository extends JpaRepository<OrderStatus, UUID> {

    Optional<OrderStatus> findById(UUID id);

    Optional<OrderStatus> findByOrderStatusIgnoreCase(String orderStatus);

    boolean existsByOrderStatusIgnoreCase(String orderStatus);
}
