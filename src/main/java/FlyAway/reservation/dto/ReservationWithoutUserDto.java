package FlyAway.reservation.dto;

import FlyAway.flight.dto.FlightDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationWithoutUserDto(
        LocalDateTime reservationDate,
        BigDecimal price,
        Integer seatNumber,
        Boolean cancelled,
        FlightDto flightDto
) {
}
