package bertcoscia.ZiplyEats_BE.repositories;

import bertcoscia.ZiplyEats_BE.entities.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ToppingsRepository extends JpaRepository<Topping, UUID>, JpaSpecificationExecutor<Topping> {

    Optional<Topping> findById(UUID id);

    List<Topping> findAllByRestaurantIdUser(UUID idRestaurant);

    boolean existsByNameAndRestaurantIdUserAndIdProductNot(String name, UUID idProduct, UUID idTopping);

    boolean existsByNameAndRestaurantIdUser(String name, UUID idRestaurant);

    List<Topping> findByRestaurantIdUser(UUID idRestaurant);
}
