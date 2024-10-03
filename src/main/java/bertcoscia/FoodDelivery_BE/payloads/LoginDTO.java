package bertcoscia.FoodDelivery_BE.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginDTO(
        @NotEmpty(message = "Email required")
            @Email(message = "Insert a valid email")
        String email,
        @NotEmpty(message = "Password required")
        String password
) {
}
