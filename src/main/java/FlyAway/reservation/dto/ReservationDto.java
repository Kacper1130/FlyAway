package FlyAway.reservation.dto;

import FlyAway.client.dto.ClientDto;
import FlyAway.flight.dto.FlightDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record ReservationDto(
        LocalDateTime reservationDate,
        BigDecimal price,
        Integer seatNumber,
        Boolean cancelled,
        ClientDto clientDto,
        FlightDto flightDto
) {
}
