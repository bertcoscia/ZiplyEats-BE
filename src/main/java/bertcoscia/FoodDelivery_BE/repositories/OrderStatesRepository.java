package bertcoscia.FoodDelivery_BE.repositories;

import bertcoscia.FoodDelivery_BE.entities.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderStatesRepository extends JpaRepository<OrderState, UUID> {

    Optional<OrderState> findById(UUID id);

    boolean existsByOrderState(String orderState);
}
