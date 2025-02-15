package FlyAway.reservation.dto;

import FlyAway.client.dto.ClientNameDto;
import FlyAway.flight.dto.FlightBasicDto;
import FlyAway.reservation.ReservationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationSummaryEmployeeDto(
        UUID id,
        ReservationStatus status,
        LocalDateTime reservationDate,
        ClientNameDto clientDto,
        FlightBasicDto flightDto

) {
}
