package bertcoscia.ZiplyEats_BE.repositories;

import bertcoscia.ZiplyEats_BE.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    Optional<Order> findById(UUID id);

    Page<Order> findAllByUserIdUser(UUID idUser, Pageable pageable);

    @Query("""
            SELECT o FROM Order o
            WHERE o.restaurant.idUser = :idRestaurant
            AND (o.orderStatus.orderStatus = 'DELIVERED' OR o.orderStatus.orderStatus = 'CANCELLED')
            """)
    Page<Order> findAllPastOrdersByRestaurant(@Param("idRestaurant") UUID idRestaurant, Pageable pageable);

    @Query("""
            SELECT o FROM Order o
            WHERE o.restaurant.idUser = :idRestaurant
            AND o.orderStatus.orderStatus != 'DELIVERED'
            AND o.orderStatus.orderStatus != 'CANCELLED'
            """)
    List<Order> findAllActiveOrdersByRestaurant(@Param("idRestaurant") UUID idRestaurant);

    @Query("""
            SELECT o FROM Order o
                WHERE o.rider.idUser = :idRider
                AND o.actualDeliveryDateTime IS NULL
                AND (o.orderStatus.orderStatus = 'RESTAURANT_ACCEPTED' OR o.orderStatus.orderStatus = 'IN_TRANSIT')
            """)
    Order findRiderCurrentActiveOrder(@Param("idRider") UUID idUser);

    @Query("""
            SELECT o FROM Order
            o WHERE o.rider IS NULL
            AND o.restaurant.city = :city
            AND o.orderStatus.orderStatus = 'RESTAURANT_ACCEPTED'
            """)
    Page<Order> findAvailableOrders(@Param("city") String city, Pageable pageable);

    @Query("""
            SELECT o FROM Order o
            WHERE o.rider.idUser = :idRider
            AND (o.orderStatus.orderStatus = 'DELIVERED' OR o.orderStatus.orderStatus = 'CANCELLED')
            """)
    Page<Order> findAllPastOrdersByRider(@Param("idRider") UUID idRider, Pageable pageable);
}
