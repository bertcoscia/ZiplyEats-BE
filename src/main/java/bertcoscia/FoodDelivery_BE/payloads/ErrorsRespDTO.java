package bertcoscia.FoodDelivery_BE.payloads;

import java.time.LocalDateTime;

public record ErrorsRespDTO(String message, LocalDateTime timestamp) {
}
