package bertcoscia.ZiplyEats_BE.payloads.edit;

import java.util.List;
import java.util.UUID;

public record EditOrderProductsDTO(
        List<UUID> toppings
) {
}
