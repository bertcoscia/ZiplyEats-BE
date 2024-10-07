package bertcoscia.FoodDelivery_BE.payloads.responses;

import java.time.LocalDateTime;

public record ErrorsRespDTO(String message, LocalDateTime timestamp) {
}
