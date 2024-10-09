package bertcoscia.FoodDelivery_BE.repositories;

import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantsRepository extends JpaRepository<Restaurant, UUID>, JpaSpecificationExecutor<Restaurant> {

    Optional<Restaurant> findByIdUser(UUID id);

    Optional<Restaurant> findByEmail(String email);

    boolean existsByNameAndAddressAndCity(String name, String address, String city);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.restaurantCategory.restaurantCategory) = LOWER(:category) AND r.city = :city")
    Page<Restaurant> findAllByCategoryAndCity(@Param("category") String category, @Param("city") String city, Pageable pageable);
}
