package bertcoscia.FoodDelivery_BE.payloads;

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
        String description
) {
}
