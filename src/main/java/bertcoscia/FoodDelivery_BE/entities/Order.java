package bertcoscia.FoodDelivery_BE.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Order {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idOrder;
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_restaurant", nullable = false)
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "id_rider")
    private Rider rider;
    @ManyToOne
    @JoinColumn(name = "id_order_state", nullable = false)
    private OrderState orderState;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "orders_products",
            joinColumns = @JoinColumn(name = "id_order"),
            inverseJoinColumns = @JoinColumn(name = "id_product")
    )
    private List<Product> productList;

    public Order(User user, Restaurant restaurant, List<Product> productList, OrderState orderState) {
        this.user = user;
        this.restaurant = restaurant;
        this.productList = productList;
        this.orderState = orderState;
    }
}
