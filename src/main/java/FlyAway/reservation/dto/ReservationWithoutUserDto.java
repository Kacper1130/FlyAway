package FlyAway.reservation.dto;

import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.dto.FlightDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationWithoutUserDto(
        LocalDateTime reservationDate,
        BigDecimal price,
        Integer seatNumber,
        CabinClass cabinClass,
        Boolean cancelled,
        FlightDto flightDto
) {
}
