package bertcoscia.ZiplyEats_BE.repositories;

import bertcoscia.ZiplyEats_BE.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderProductsRepository extends JpaRepository<OrderProduct, UUID>, JpaSpecificationExecutor<OrderProduct> {

    Optional<OrderProduct> findByIdOrderProduct(UUID idOrderProduct);

    List<OrderProduct> findByOrderIdOrder(UUID idOrder);
}
