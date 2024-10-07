package bertcoscia.FoodDelivery_BE.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<OrderProduct> orderProductList = new ArrayList<>();
    @Column(name = "delivery_address")
    private String deliveryAddress;
    @Column(name = "creation_date")
    private LocalDateTime creationDateTime;
    @Column(name = "delivery_dateTime")
    private LocalDateTime deliveryDateTime;
    @Column(name = "total_price")
    private double totalPrice;

    public Order(User user, Restaurant restaurant, OrderStatus orderStatus, String deliveryAddress, LocalDateTime deliveryDateTime) {
        this.user = user;
        this.restaurant = restaurant;
        this.orderStatus = orderStatus;
        this.deliveryAddress = deliveryAddress;
        this.creationDateTime = LocalDateTime.now();
        this.deliveryDateTime = deliveryDateTime;
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (OrderProduct orderProduct : orderProductList) totalPrice += orderProduct.getPrice();
        return totalPrice;
    }


    public void addOrderProduct(Product product, List<Topping> toppings) {
        OrderProduct orderProduct = new OrderProduct(this, product, toppings);
        this.orderProductList.add(orderProduct);
    }
}