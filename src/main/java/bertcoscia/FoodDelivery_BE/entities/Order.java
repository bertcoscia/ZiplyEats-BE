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
    @JoinColumn(name = "id_order_status", nullable = false)
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProductList;
    @Column(name = "delivery_address")
    private String deliveryAddress;

    public Order(User user, Restaurant restaurant, String deliveryAddress, OrderStatus orderStatus) {
        this.user = user;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.orderStatus = orderStatus;
    }

    public void addOrderProduct(Product product, List<Topping> toppings) {
        OrderProduct orderProduct = new OrderProduct(this, product, toppings);
        this.orderProductList.add(orderProduct);
    }
}