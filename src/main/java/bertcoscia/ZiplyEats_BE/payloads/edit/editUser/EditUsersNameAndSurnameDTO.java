package bertcoscia.ZiplyEats_BE.payloads.edit.editUser;

import jakarta.validation.constraints.NotEmpty;

public record EditUsersNameAndSurnameDTO(
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @NotEmpty(message = "Surname cannot be empty")
        String surname
) {
}
