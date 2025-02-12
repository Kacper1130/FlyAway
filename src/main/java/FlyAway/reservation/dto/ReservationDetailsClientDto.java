package FlyAway.reservation.dto;

import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.dto.FlightDetailsClientDto;
import FlyAway.reservation.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationDetailsClientDto(
        UUID id,
        ReservationStatus status,
        CabinClass cabinClass,
        Integer seatNumber,
        BigDecimal price,
        LocalDateTime reservationDate,
        FlightDetailsClientDto flightDto

) {
}
