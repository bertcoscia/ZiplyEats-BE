package bertcoscia.FoodDelivery_BE.repositories;

import bertcoscia.FoodDelivery_BE.entities.Rider;
import bertcoscia.FoodDelivery_BE.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    Optional<Rider> findRiderByIdUser(UUID id);

    // TODO: ADD ID_ROLE = RIDER
/*    @Query(value = "SELECT * FROM Users WHERE id_role = ")
    Page<Rider> findAllRiders(Pageable pageable, Specification spec);*/

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

}
