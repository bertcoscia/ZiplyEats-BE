package bertcoscia.ZiplyEats_BE.payloads.edit.editUser;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EditUsersPhoneNumberDTO(
        @NotEmpty(message = "Phone number cannot be empty")
        @Size(min = 9, max = 10, message = "Phone number must contain between 9 and 10 digits")
        String phoneNumber
) {
}
