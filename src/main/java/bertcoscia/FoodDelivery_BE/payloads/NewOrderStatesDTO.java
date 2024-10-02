package bertcoscia.FoodDelivery_BE.payloads;

import jakarta.validation.constraints.NotEmpty;

public record NewOrderStatesDTO(
        @NotEmpty(message = "Order state cannot be empty")
        String orderState
) {
}
