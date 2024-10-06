package bertcoscia.FoodDelivery_BE.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewOrdersDTO(
        @NotEmpty(message = "Restaurant id required")
        @Size(min = 36, max = 36, message = "Restaurant id must be 36 characters long")
        String idRestaurant,
        @NotEmpty(message = "The order must contain at least one product")
        List<@Size(min = 36, max = 36, message = "Product id must be 36 characters long") String> productList,
        @NotEmpty(message = "Delivery address required")
        String deliveryAddress
) {
}
