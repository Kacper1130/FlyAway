package FlyAway.reservation.dto;

import FlyAway.client.dto.ClientDto;
import FlyAway.flight.dto.FlightDto;

import java.time.LocalDateTime;


public record ReservationDto(
        LocalDateTime reservationDate,
        Long price,
        Long seatNumber,
        Boolean cancelled,
        ClientDto clientDto,
        FlightDto flightDto
) {
}
