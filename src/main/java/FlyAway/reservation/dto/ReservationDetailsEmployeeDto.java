package FlyAway.reservation.dto;

import FlyAway.client.dto.ClientDto;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.dto.FlightDetailsEmployeeDto;
import FlyAway.reservation.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationDetailsEmployeeDto(
        UUID id,
        ReservationStatus status,
        ClientDto clientDto,
        CabinClass cabinClass,
        Integer seatNumber,
        BigDecimal price,
        LocalDateTime reservationDate,
        FlightDetailsEmployeeDto flightDto
) {
}
