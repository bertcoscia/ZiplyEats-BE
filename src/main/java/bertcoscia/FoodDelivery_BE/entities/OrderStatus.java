package bertcoscia.FoodDelivery_BE.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "order_statuses")
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"idOrderStatus"})
public class OrderStatus {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idOrderStatus;
    @Column(name = "order_status")
    private String orderStatus;

    public OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
