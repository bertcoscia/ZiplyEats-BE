package bertcoscia.FoodDelivery_BE.payloads.newEntities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record NewOrdersDTO(
        @NotNull(message = "Restaurant ID required")
        UUID idRestaurant,
        @NotEmpty(message = "The order must contain at least one product")
        List<NewOrderProductsDTO> orderProductList,
        @NotEmpty(message = "Delivery address required")
        String deliveryAddress,
        @NotNull(message = "Delivery date time required")
        LocalDateTime deliveryDateTime
) {
}