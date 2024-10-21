package bertcoscia.ZiplyEats_BE.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Product {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    protected UUID idProduct;
    protected String name;
    protected double price;
    protected String description;
    @ManyToOne
    @JoinColumn(name = "id_restaurant")
    @JsonIgnoreProperties({"email", "phoneNumber", "address", "city", "userRole", "avatarUrl", "rating", "restaurantCategory", "username"})
    protected Restaurant restaurant;
    protected String imageUrl;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private ProductCategory productCategory;

    public Product(String name, double price, String description, Restaurant restaurant, ProductCategory productCategory) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.restaurant = restaurant;
        this.productCategory = productCategory;
    }

    public Product(String name, double price, String description, Restaurant restaurant, String imageUrl, ProductCategory productCategory) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.restaurant = restaurant;
        this.imageUrl = imageUrl;
        this.productCategory = productCategory;
    }

    public Product(String name, double price, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
        this.description = null;
    }
}