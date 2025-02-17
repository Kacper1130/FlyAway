package FlyAway.flight.dto;

import FlyAway.flight.airport.dto.AirportBasicDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record FlightDetailsEmployeeDto(
        UUID id,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate,
        AirportBasicDto departureAirportDto,
        AirportBasicDto arrivalAirportDto
) {
}
