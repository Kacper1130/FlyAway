package FlyAway.reservation.dto;

import FlyAway.flight.dto.FlightDto;
import FlyAway.user.dto.UserDto;

import java.time.LocalDateTime;


public record ReservationDto(
        LocalDateTime reservationDate,
        Long price,
        Long seatNumber,
        Boolean cancelled,
        UserDto userDto,
        FlightDto flightDto
) {
}
