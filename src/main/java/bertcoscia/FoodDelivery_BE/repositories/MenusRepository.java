package bertcoscia.FoodDelivery_BE.repositories;

import bertcoscia.FoodDelivery_BE.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenusRepository extends JpaRepository<Menu, UUID> {

    Optional<Menu> findById(UUID id);

    Optional<Menu> findByRestaurantIdUser(UUID idUser);

    boolean existsByRestaurantIdUser(UUID idUser);
}
