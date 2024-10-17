package bertcoscia.ZiplyEats_BE.repositories;

import bertcoscia.ZiplyEats_BE.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    Optional<Order> findById(UUID id);

    Page<Order> findAllByUserIdUser(UUID idUser, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.restaurant.idUser = :idRestaurant AND o.orderStatus.orderStatus = 'DELIVERED' or o.orderStatus.orderStatus = 'CANCELLED'")
    Page<Order> findAllPastOrdersByRestaurant(UUID idRestaurant, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.restaurant.idUser = :idRestaurant AND o.orderStatus.orderStatus != 'DELIVERED' AND o.orderStatus.orderStatus != 'CANCELLED'")
    List<Order> findAllActiveOrdersByRestaurant(UUID idRestaurant);
}
