package bertcoscia.FoodDelivery_BE.payloads.newEntities;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record NewOrderProductsDTO(
        @NotNull(message = "Product ID required")
        UUID idProduct,
        List<UUID> toppings
) {
}
