package bertcoscia.ZiplyEats_BE.repositories;

import bertcoscia.ZiplyEats_BE.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCategoriesRepository extends JpaRepository<ProductCategory, UUID> {

    Optional<ProductCategory> findByIdProductCategory(UUID id);

    Optional<ProductCategory> findByRestaurantIdUserAndProductCategoryIgnoreCase(UUID idRestaurant, String productCategory);

    boolean existsByProductCategoryAndRestaurantIdUser(String restaurantCategory, UUID idRestaurant);

    List<ProductCategory> findAllByRestaurantIdUser(UUID idRestaurant);
}
