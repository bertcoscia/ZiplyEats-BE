package bertcoscia.FoodDelivery_BE.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"password", "enabled", "accountNonLocked", "credentialsNonExpired", "accountNonExpired", "username", "authorities", "userRole"})
public class Restaurant implements UserDetails {
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
    @ManyToOne
    @JoinColumn(name = "id_role")
    private UserRole userRole;

    public Restaurant(String name, String address, String city, String email, String password, String phoneNumber, RestaurantCategory restaurantCategory, UserRole userRole) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.restaurantCategory = restaurantCategory;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.userRole.getUserRole()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
