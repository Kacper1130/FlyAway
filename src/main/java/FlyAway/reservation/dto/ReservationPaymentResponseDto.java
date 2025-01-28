package FlyAway.reservation.dto;

public record ReservationPaymentResponseDto(
        ReservationDto reservation,
        String paymentUrl
) {}
