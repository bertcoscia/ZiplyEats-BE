package bertcoscia.ZiplyEats_BE.payloads.edit.editUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record EditUsersEmailDTO(
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Insert a valid email")
        String email
) {

}
