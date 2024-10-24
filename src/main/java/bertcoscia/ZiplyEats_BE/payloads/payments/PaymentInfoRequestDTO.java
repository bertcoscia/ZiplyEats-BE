package bertcoscia.ZiplyEats_BE.payloads.payments;

public record PaymentInfoRequestDTO(
        int amount,
        String currency,
        String receiptEmail
) {
}
