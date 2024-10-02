package bertcoscia.FoodDelivery_BE.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "order_states")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderState {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idOrderState;
    @Column(name = "order_state")
    private String orderState;

    public OrderState(String orderState) {
        this.orderState = orderState;
    }
}
