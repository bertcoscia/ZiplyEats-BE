package bertcoscia.FoodDelivery_BE.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "toppings")
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"description"})
public class Topping extends Product {
    @Transient
    private String description;
    @Transient
    private String imageUrl;

    public Topping(String name, double price, Restaurant restaurant) {
        super(name, price, restaurant);
    }
}