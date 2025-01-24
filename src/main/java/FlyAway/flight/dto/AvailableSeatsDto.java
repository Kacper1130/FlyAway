package FlyAway.flight.dto;

import FlyAway.flight.aircraft.CabinClass;

import java.util.List;
import java.util.UUID;

public record AvailableSeatsDto(
        UUID id,
        CabinClass cabinClass,
        List<Integer> availableSeats

) {
}
