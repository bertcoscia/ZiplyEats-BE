package bertcoscia.ZiplyEats_BE.repositories;

import bertcoscia.ZiplyEats_BE.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Optional<Product> findById(UUID id);

    boolean existsByNameAndRestaurantIdUser(String name, UUID idRestaurant);

    List<Product> findAllByRestaurantIdUserAndDescriptionIsNotNull(UUID idRestaurant);

    boolean existsByNameAndRestaurantIdUserAndIdProductNot(String name, UUID restaurantId, UUID id);
}
