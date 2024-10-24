package bertcoscia.ZiplyEats_BE.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties({"address", "userRole", "username", "avatarUrl"})
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_restaurant", nullable = false)
    @JsonIgnoreProperties({"userRole", "username", "rating", "restaurantCategory"})
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "id_rider")
    @JsonIgnoreProperties({"address", "userRole", "username", "rating", "email", "busyWithOrder"})
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
    @Column(name = "requested_delivery_dateTime")
    private LocalDateTime requestedDeliveryDateTime;
    @Column(name = "total_price")
    private double totalPrice;
    @Column(name = "actual_delivery_DateTime")
    private LocalDateTime actualDeliveryDateTime;


    public Order(User user, Restaurant restaurant, OrderStatus orderStatus, String deliveryAddress, LocalDateTime deliveryDateTime) {
        this.user = user;
        this.restaurant = restaurant;
        this.orderStatus = orderStatus;
        this.deliveryAddress = deliveryAddress;
        this.creationDateTime = LocalDateTime.now();
        this.requestedDeliveryDateTime = deliveryDateTime;
        this.actualDeliveryDateTime = null;
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (OrderProduct orderProduct : orderProductList) totalPrice += orderProduct.getPrice();
        return totalPrice;
    }

    public double calculateTotalPriceStripe() {
        double totalPrice = 0;
        for (OrderProduct orderProduct : orderProductList) totalPrice += orderProduct.getPrice();
        return totalPrice * 100;
    }

    public void addOrderProduct(Product product, List<Topping> toppings) {
        OrderProduct orderProduct = new OrderProduct(this, product, toppings);
        this.orderProductList.add(orderProduct);
    }

    public List<OrderProduct> getOrderProducts() {
        return this.orderProductList;
    }
}