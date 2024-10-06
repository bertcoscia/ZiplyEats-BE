package bertcoscia.FoodDelivery_BE.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_menu")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private RestaurantCategory restaurantCategory;

    public Restaurant(String name, String email, String password, String phoneNumber, String address, String city, UserRole userRole, RestaurantCategory restaurantCategory) {
        super(name, email, password, phoneNumber, address, city, userRole);
        this.restaurantCategory = restaurantCategory;
    }

    @Override
    public String getSurname() {
        return null;
    }
}
