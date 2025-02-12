package FlyAway.flight.dto;

import FlyAway.flight.aircraft.dto.AircraftDto;
import FlyAway.flight.airport.dto.AirportBasicDto;

import java.time.LocalDateTime;

public record FlightDetailsClientDto(
        LocalDateTime departureDate,
        LocalDateTime arrivalDate,
        AirportBasicDto departureAirportDto,
        AirportBasicDto arrivalAirportDto,
        AircraftDto aircraftDto

) {
}
