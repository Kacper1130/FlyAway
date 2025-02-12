package FlyAway.flight.dto;

import java.time.LocalDateTime;

public record FlightBasicDto(
        String departureCity,
        String arrivalCity,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate
) {
}
