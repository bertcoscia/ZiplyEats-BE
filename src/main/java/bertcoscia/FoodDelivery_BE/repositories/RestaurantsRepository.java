package bertcoscia.FoodDelivery_BE.repositories;

import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantsRepository extends JpaRepository<Restaurant, UUID> {

    Optional<Restaurant> findById(UUID id);

    Optional<Restaurant> findByEmail(String email);

    boolean existsByNameAndAddressAndCity(String name, String address, String city);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

}
