package bertcoscia.FoodDelivery_BE.payloads;

import jakarta.validation.constraints.NotEmpty;

public record NewRestaurantCategoriesDTO(
        @NotEmpty(message = "Restaurant category cannot be empty")
        String restaurantCategory
) {
}
