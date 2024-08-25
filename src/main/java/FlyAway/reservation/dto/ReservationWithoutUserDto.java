package FlyAway.reservation.dto;

import FlyAway.flight.dto.FlightDto;

import java.time.LocalDateTime;

public record ReservationWithoutUserDto(
        LocalDateTime reservationDate,
        Long price,
        Long seatNumber,
        Boolean cancelled,
        FlightDto flightDto
) {
}
