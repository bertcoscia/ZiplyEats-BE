package bertcoscia.ZiplyEats_BE.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "product_categories")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductCategory {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idProductCategory;
    @Column(name = "product_category")
    private String productCategory;
    @ManyToOne
    @JoinColumn(name = "id_restaurant")
    private Restaurant restaurant;

    public ProductCategory(String productCategory, Restaurant restaurant) {
        this.productCategory = productCategory;
        this.restaurant = restaurant;
    }
}
