package FlyAway.flight.airport.dto;

import java.util.UUID;

public record AirportDto(
        UUID id,
        String name,
        String IATACode,
        String country,
        String city,
        boolean enabled
) {
}
