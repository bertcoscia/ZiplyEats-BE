package bertcoscia.FoodDelivery_BE.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductsDTO (
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @NotNull(message = "Price required")
        double price,
        @NotEmpty(message = "Description cannot be empty")
        String description
) {
}
