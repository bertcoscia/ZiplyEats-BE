package bertcoscia.FoodDelivery_BE.payloads;

import jakarta.validation.constraints.NotEmpty;

public record NewUserRolesDTO(
        @NotEmpty(message = "User role cannot be empty")
        String userRole
) {
}
