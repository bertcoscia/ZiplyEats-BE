package bertcoscia.ZiplyEats_BE.payloads.edit.editUser;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EditUsersAdressDTO(
        @NotEmpty(message = "Address cannot be empty")
        String address,
        @NotEmpty(message = "City cannot be empty")
        String city,
        @NotNull(message = "Latitude required")
        double latitude,
        @NotNull(message = "Longitude required")
        double longitude
) {
}
