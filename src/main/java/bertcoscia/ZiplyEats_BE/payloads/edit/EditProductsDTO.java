package bertcoscia.ZiplyEats_BE.payloads.edit;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EditProductsDTO(
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @NotNull(message = "Price required")
            @DecimalMin(value = "0.01", message = "Price must be bigger than 0.00")
        double price,
        @NotEmpty(message = "Description cannot be empty")
        String description,
        @NotEmpty(message = "Product category cannot be empty")
        String productCategory,
        @NotNull(message = "Must specify if product can have toppings")
        Boolean canHaveToppings
) {
}
