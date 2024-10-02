package bertcoscia.FoodDelivery_BE.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewMenusDTO(
        @NotEmpty(message = "Restaurant id required")
            @Size(min = 36, max = 36, message = "Restaurant id must be 36 characters long")
        String idRestaurant
) {
}
