package bertcoscia.ZiplyEats_BE.payloads.newEntities;

import jakarta.validation.constraints.NotEmpty;

public record NewProductCategoriesDTO(
        @NotEmpty(message = "Product category cannot be empty")
        String productCategory
) {
}
