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
    private String email;
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_menu")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private RestaurantCategory restaurantCategory;

    public Restaurant(String name, String address, String city, String email, String password, String phoneNumber, Menu menu, RestaurantCategory restaurantCategory) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.rating = 0;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.menu = menu;
        this.restaurantCategory = restaurantCategory;
    }
}
