package bertcoscia.FoodDelivery_BE.repositories;

import bertcoscia.FoodDelivery_BE.entities.Topping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ToppingsRepository extends JpaRepository<Topping, UUID>, JpaSpecificationExecutor<Topping> {

    Optional<Topping> findById(UUID id);

    Page<Topping> findAllByRestaurantIdUser(UUID idRestaurant, Pageable pageable);

    boolean existsByNameAndRestaurantIdUserAndIdProductNot(String name, UUID idProduct, UUID idTopping);

    boolean existsByNameAndRestaurantIdUser(String name, UUID idRestaurant);

    Page<Topping> findByRestaurantIdUser(UUID idRestaurant, Pageable pageable);
}
