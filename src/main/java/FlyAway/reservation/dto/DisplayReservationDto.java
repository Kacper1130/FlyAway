package FlyAway.reservation.dto;

import FlyAway.flight.aircraft.CabinClass;
import FlyAway.reservation.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DisplayReservationDto(
        UUID id,
        LocalDateTime reservationDate,
        BigDecimal price,
        CabinClass cabinClass,
        Integer seatNumber,
        ReservationStatus status,
        Long clientId,
        UUID flightId
) {
}
