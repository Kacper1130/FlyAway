package FlyAway.reservation.dto;

import FlyAway.client.dto.ClientDto;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.dto.FlightDto;
import FlyAway.reservation.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record ReservationDto(
        LocalDateTime reservationDate,
        BigDecimal price,
        Integer seatNumber,
        CabinClass cabinClass,
        ReservationStatus status,
        ClientDto clientDto,
        FlightDto flightDto
) {
}
