package bertcoscia.FoodDelivery_BE.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "restaurants")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Restaurant {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idRestaurant;
    private String name;
    private String address;
    private String city;
    private double rating;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_menu")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private RestaurantCategory restaurantCategory;

    public Restaurant(String name, String address, String city, double rating, RestaurantCategory restaurantCategory) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.rating = rating;
        this.restaurantCategory = restaurantCategory;
    }
}
