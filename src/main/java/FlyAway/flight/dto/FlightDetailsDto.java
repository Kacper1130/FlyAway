package FlyAway.flight.dto;

import FlyAway.flight.aircraft.Aircraft;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.airport.dto.AirportDto;
import FlyAway.reservation.dto.ReservationWithoutFlightDto;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record FlightDetailsDto(
        @NotNull
        UUID id,
        @NotNull(message = "departureAirportDto can not be null")
        @Valid
        AirportDto departureAirportDto,
        @NotNull(message = "arrivalAirportDto can not be null")
        @Valid
        AirportDto arrivalAirportDto,
        @NotNull(message = "departureDate can not be null")
        LocalDateTime departureDate,
        @NotNull(message = "arrivalDate can not be null")
        LocalDateTime arrivalDate,
        @NotNull(message = "aircraft can not be null")
        @Valid
        Aircraft aircraft,
        @NotNull(message = "cabinClassPrices can not be null")
        Map<CabinClass, BigDecimal> cabinClassPrices,
        @OneToMany
        List<ReservationWithoutFlightDto> reservations
) {
}
