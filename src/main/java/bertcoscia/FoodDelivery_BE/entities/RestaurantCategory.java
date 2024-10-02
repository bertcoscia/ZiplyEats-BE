package bertcoscia.FoodDelivery_BE.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "restaurant_categories")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RestaurantCategory {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idCategory;
    @Column(name = "restaurant_category")
    private String restaurantCategory;

    public RestaurantCategory(String restaurantCategory) {
        this.restaurantCategory = restaurantCategory;
    }
}
