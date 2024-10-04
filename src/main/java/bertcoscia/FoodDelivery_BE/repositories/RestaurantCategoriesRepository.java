package bertcoscia.FoodDelivery_BE.repositories;

import bertcoscia.FoodDelivery_BE.entities.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantCategoriesRepository extends JpaRepository<RestaurantCategory, UUID> {

    Optional<RestaurantCategory> findById(UUID id);

    Optional<RestaurantCategory> findByRestaurantCategoryIgnoreCase(String restaurantCategory);

    boolean existsByRestaurantCategory(String restaurantCategory);
}
