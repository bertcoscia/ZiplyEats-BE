package bertcoscia.FoodDelivery_BE.payloads;

import jakarta.validation.constraints.NotEmpty;

public record NewOrderStatusesDTO(
        @NotEmpty(message = "Order status cannot be empty")
        String orderStatus
) {
}
