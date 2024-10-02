package bertcoscia.FoodDelivery_BE.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("rider")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Rider extends User {
    private double rating;

    public Rider(String name, String surname, String email, String password, String phoneNumber, String address, String city, UserRole userRole) {
        super(name, surname, email, password, phoneNumber, address, city, userRole);
        this.rating = 0;
    }
}