package bertcoscia.ZiplyEats_BE.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurants")
@DiscriminatorValue("RESTAURANT")
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"surname", "password", "role", "authorities", "enabled", "accountNonLocked", "accountNonExpired", "credentialsNonExpired"})
public class Restaurant extends User {
    @Transient
    private String surname;
    private double rating;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private RestaurantCategory restaurantCategory;

    public Restaurant(String name, String email, String password, String phoneNumber, String address, String city, UserRole userRole, RestaurantCategory restaurantCategory, double latitude, double longitude) {
        super(name, email, password, phoneNumber, address, city, userRole, latitude, longitude);
        this.restaurantCategory = restaurantCategory;
    }

    @Override
    public String getSurname() {
        return null;
    }
}
