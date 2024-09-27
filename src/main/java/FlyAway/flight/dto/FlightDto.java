package FlyAway.flight.dto;

import FlyAway.flight.aircraft.Aircraft;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.airport.Airport;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record FlightDto(
        @NotNull
        Airport departureAirport,
        @NotNull
        Airport arrivalAirport,
        @NotNull
        LocalDateTime departureDate,
        @NotNull
        LocalDateTime arrivalDate,
        @NotNull
        Aircraft aircraft,
        @NotNull
        Map<CabinClass, BigDecimal> cabinClassPrices
) {
}
