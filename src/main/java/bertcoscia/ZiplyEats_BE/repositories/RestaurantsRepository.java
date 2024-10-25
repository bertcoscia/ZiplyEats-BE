package bertcoscia.ZiplyEats_BE.repositories;

import bertcoscia.ZiplyEats_BE.entities.Restaurant;
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

    @Query("""
            SELECT r FROM Restaurant r
            WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))
            AND LOWER(r.city) = LOWER(:city)
            """)
    Optional<Restaurant> findByNameIgnoreCaseAndCityIgnoreCase(@Param("name") String name, @Param("city") String city);

    boolean existsByNameAndAddressAndCity(String name, String address, String city);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("""
    SELECT r FROM Restaurant r
    WHERE LOWER(r.restaurantCategory.restaurantCategory) = LOWER(:category) AND r.city = :city
    """)
    Page<Restaurant> findAllByCategoryAndCity(@Param("category") String category, @Param("city") String city, Pageable pageable);

    @Query("""
    SELECT r FROM Restaurant r
    WHERE r.restaurantCategory.restaurantCategory = :category
    AND r.city = :city
    ORDER BY CASE WHEN LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) THEN 0 ELSE 1 END
    """)
    Page<Restaurant> findByNameAndCityAndCategoryAndSimilar(@Param("name") String name, @Param("city") String city, @Param("category") String category, Pageable pageable);

    @Query("SELECT r FROM Restaurant r WHERE r.city = :city")
    Page<Restaurant> findAllByCity(@Param("city") String city, Pageable pageable);
}
