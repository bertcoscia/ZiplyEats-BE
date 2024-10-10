package bertcoscia.ZiplyEats_BE.payloads.newEntities;

import jakarta.validation.constraints.NotEmpty;

public record NewUserRolesDTO(
        @NotEmpty(message = "User role cannot be empty")
        String userRole
) {
}
