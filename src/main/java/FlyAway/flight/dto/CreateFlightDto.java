package FlyAway.flight.dto;

import java.time.LocalDateTime;

public record CreateFlightDto(
        String departureCity,
        String arrivalCity,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate,
        String airline
) {
}
