package bertcoscia.FoodDelivery_BE.payloads.newEntities;

import jakarta.validation.constraints.NotEmpty;

public record NewOrderStatusesDTO(
        @NotEmpty(message = "Order status cannot be empty")
        String orderStatus
) {
}
