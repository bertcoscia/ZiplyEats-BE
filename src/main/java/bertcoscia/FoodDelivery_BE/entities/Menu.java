package bertcoscia.FoodDelivery_BE.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menus")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Menu {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idMenu;
    @OneToOne(mappedBy = "menu")
    private Restaurant restaurant;
    @ManyToMany(mappedBy = "menuList")
    private List<Product> productList;

    public Menu(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
