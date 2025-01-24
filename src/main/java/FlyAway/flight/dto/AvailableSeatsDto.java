package FlyAway.flight.dto;

import FlyAway.flight.aircraft.CabinClass;

import java.util.Map;
import java.util.UUID;

public record AvailableSeatsDto(
        UUID id,
        CabinClass cabinClass,
        Map<Integer, Boolean> availableSeats

) {
}
