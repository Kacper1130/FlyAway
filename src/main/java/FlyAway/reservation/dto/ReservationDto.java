package FlyAway.reservation.dto;

import FlyAway.client.dto.ClientDto;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.dto.FlightDto;
import FlyAway.reservation.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public record ReservationDto(
        UUID id,
        LocalDateTime reservationDate,
        BigDecimal price,
        Integer seatNumber,
        CabinClass cabinClass,
        ReservationStatus status,
        ClientDto clientDto,
        FlightDto flightDto
) {
}
