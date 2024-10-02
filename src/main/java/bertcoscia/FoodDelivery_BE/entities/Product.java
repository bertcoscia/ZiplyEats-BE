package bertcoscia.FoodDelivery_BE.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Product {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idProduct;
    private String name;
    private double price;
    private String description;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "menus_products",
            joinColumns = @JoinColumn(name = "id_product"),
            inverseJoinColumns = @JoinColumn(name = "id_menu")
    )
    private List<Menu> menuList;
    @ManyToMany(mappedBy = "productList")
    private List<Order> orderList;

    public Product(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
