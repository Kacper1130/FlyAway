package FlyAway.flight.dto;

import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.airport.dto.AirportCityIataCodeDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record FlightSummaryEmployeeDto(
        UUID id,
        AirportCityIataCodeDto departureAirportDto,
        AirportCityIataCodeDto arrivalAirportDto,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate,
        Map<CabinClass, BigDecimal> cabinClassPrices
) {
}
