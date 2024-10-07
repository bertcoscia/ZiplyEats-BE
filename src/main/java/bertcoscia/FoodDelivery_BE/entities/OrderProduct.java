package bertcoscia.FoodDelivery_BE.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders_products")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderProduct {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idOrderProduct;
    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;
    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;
    @ManyToMany
    @JoinTable(
            name = "orders_products_toppings",
            joinColumns = @JoinColumn(name = "id_order_product"),
            inverseJoinColumns = @JoinColumn(name = "id_topping")
    )
    private List<Topping> toppings;

    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }

    public OrderProduct(Order order, Product product, List<Topping> toppings) {
        this.order = order;
        this.product = product;
        this.toppings = toppings;
    }
}