package bertcoscia.ZiplyEats_BE.payloads.newEntities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewProductsDTO (
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @NotNull(message = "Price required")
        double price,
        @NotEmpty(message = "Description cannot be empty")
        String description,
        @NotEmpty(message = "Product category cannot be empty")
        String productCategory,
        @NotNull(message = "Must specify if product can have toppings")
        Boolean canHaveToppings
) {
}
