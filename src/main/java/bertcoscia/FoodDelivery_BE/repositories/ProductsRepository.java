package bertcoscia.FoodDelivery_BE.repositories;

import bertcoscia.FoodDelivery_BE.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Optional<Product> findById(UUID id);

    boolean existsByNameAndRestaurantIdUser(String name, UUID idRestaurant);

    Page<Product> findAllByRestaurantIdUserAndDescriptionIsNotNull(UUID idRestaurant, Pageable pageable);

    boolean existsByNameAndRestaurantIdUserAndIdProductNot(String name, UUID restaurantId, UUID id);
}
