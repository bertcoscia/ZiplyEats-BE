package bertcoscia.FoodDelivery_BE.repositories;

import bertcoscia.FoodDelivery_BE.entities.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ToppingsRepository extends JpaRepository<Topping, UUID> {

    Optional<Topping> findById(UUID id);

    boolean existsByName(String name);
}
