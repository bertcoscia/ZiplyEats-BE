package bertcoscia.ZiplyEats_BE.payloads.edit;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EditRestaurantsDTO(
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @NotEmpty(message = "Address cannot be empty")
        String address,
        @NotEmpty(message = "City cannot be empty")
        String city,
        @NotEmpty(message = "Email cannot be empty")
            @Email(message = "Insert a valid email")
        String email,
        @NotEmpty(message = "Phone number cannot be empty")
            @Size(min = 9, max = 10, message = "Phone number must contain between 9 and 10 digits")
        String phoneNumber,
        @NotEmpty(message = "Category cannot be empty")
        String restaurantCategory
) {}