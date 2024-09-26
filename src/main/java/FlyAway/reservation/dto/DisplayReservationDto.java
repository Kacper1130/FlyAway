package FlyAway.reservation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DisplayReservationDto(
        UUID id,
        LocalDateTime reservationDate,
        BigDecimal price,
        Integer seatNumber,
        Boolean cancelled,
        Long clientId,
        UUID flightId
) {
}
