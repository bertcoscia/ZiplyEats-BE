package bertcoscia.ZiplyEats_BE.payloads.edit;

import jakarta.validation.constraints.NotEmpty;

public record EditProductCategoriesDTO(
        @NotEmpty(message = "Product category cannot be empty")
        String productCategory
) {
}
