package bertcoscia.FoodDelivery_BE.payloads.edit;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record EditOrdersDTO(
        @NotEmpty(message = "The order must contain at least one product")
        List<UUID> productList,
        @NotEmpty(message = "Delivery address required")
        String deliveryAddress
) {
}
