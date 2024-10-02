package bertcoscia.FoodDelivery_BE.payloads;

import jakarta.validation.constraints.*;

public record NewRestaurantsDTO(
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @NotEmpty(message = "Address cannot be empty")
        String address,
        @NotEmpty(message = "City cannot be empty")
        String city,
        @NotEmpty(message = "Email cannot be empty")
                @Email(message = "Insert a valid email")
        String email,
        @NotBlank(message = "Password cannot be empty")
                @Size(min = 8, message = "Password must be at least 8 characters long")
                @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
                @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
                @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
                @Pattern(regexp = ".*[@#$%^&+=!].*", message = "Password must contain at least one special character (@#$%^&+=!)")
        String password,
        @NotEmpty(message = "Phone number cannot be empty")
                @Size(min = 9, max = 10, message = "Phone number must contain between 9 and 10 digits")
        String phoneNumber,
        @Size(min = 36, max = 36, message = "Restaurant id must be 36 characters long")
        String idCategory,
        String restaurantCategory
) {
}
