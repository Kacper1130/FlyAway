package FlyAway.reservation.dto;

import FlyAway.client.dto.ClientDto;
import FlyAway.flight.aircraft.CabinClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationWithoutFlightDto(
        LocalDateTime reservationDate,
        BigDecimal price,
        Integer seatNumber,
        CabinClass cabinClass,
        boolean cancelled,
        ClientDto clientDto
) {
}
