package bertcoscia.ZiplyEats_BE.payloads.edit.editUser;

import jakarta.validation.constraints.NotEmpty;

public record EditRestaurantsNameDTO(
        @NotEmpty(message = "Name cannot be empty")
        String name
){
}
