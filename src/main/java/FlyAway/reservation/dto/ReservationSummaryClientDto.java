package FlyAway.reservation.dto;

import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.dto.FlightBasicDto;
import FlyAway.reservation.ReservationStatus;

import java.util.UUID;

public record ReservationSummaryClientDto(
        UUID id,
        ReservationStatus status,
        CabinClass cabinClass,
        FlightBasicDto flightDto

) {
}
