package bertcoscia.ZiplyEats_BE.payloads.newEntities;

import jakarta.validation.constraints.NotEmpty;

public record NewRestaurantCategoriesDTO(
        @NotEmpty(message = "Restaurant category cannot be empty")
        String restaurantCategory
) {
}
