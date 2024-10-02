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
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("user")
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"password", "enabled", "accountNonLocked", "credentialsNonExpired", "accountNonExpired"})
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idUser;
    private String name;
    private String surname;
    private String email;
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String address;
    private String city;
    @ManyToOne
    @JoinColumn(name = "id_role")
    private UserRole userRole;

    public User(String name, String surname, String email, String password, String phoneNumber, String address, String city, UserRole userRole) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
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