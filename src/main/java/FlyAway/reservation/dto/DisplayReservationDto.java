package FlyAway.reservation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DisplayReservationDto(
        UUID id,
        LocalDateTime reservationDate,
        Long price,
        Long seatNumber,
        Boolean cancelled,
        Long clientId,
        UUID flightId
) {
}
