package bertcoscia.ZiplyEats_BE.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "restaurant_categories")
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"idCategory"})
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
