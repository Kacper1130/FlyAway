package FlyAway.flight;

import FlyAway.flight.dto.FlightDto;
import FlyAway.reservation.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlightMapperTest {

    private FlightMapper flightMapper = Mappers.getMapper(FlightMapper.class);

    @Test
    void shouldMapFlightDtoToFlight() {

        FlightDto flightDto = new FlightDto(
                "Cracow",
                "Warsaw",
                LocalDateTime.of(2024, Month.APRIL, 24, 18, 30),
                LocalDateTime.of(2024, Month.APRIL, 24, 19, 30),
                "Ryanair"
        );

        Flight flight = flightMapper.flightDtoToFlight(flightDto);

        Assertions.assertEquals(flightDto.departureCity(), flight.getDepartureCity());
        Assertions.assertEquals(flightDto.arrivalCity(), flight.getArrivalCity());
        Assertions.assertEquals(flightDto.departureDate(), flight.getDepartureDate());
        Assertions.assertEquals(flightDto.arrivalDate(), flight.getArrivalDate());
        Assertions.assertEquals(flightDto.airline(), flight.getAirline());
    }

    @Test
    void shouldMapFlightToFlightDto() {

        Flight flight = new Flight(
                UUID.randomUUID(),
                "Cracow",
                "Warsaw",
                LocalDateTime.of(2024, Month.APRIL, 24, 18, 30),
                LocalDateTime.of(2024, Month.APRIL, 24, 19, 30),
                "Ryanair",
                new ArrayList<Reservation>()
        );

        FlightDto flightDto = flightMapper.flightToFlightDto(flight);

        Assertions.assertEquals(flight.getDepartureCity(), flightDto.departureCity());
        Assertions.assertEquals(flight.getArrivalCity(), flightDto.arrivalCity());
        Assertions.assertEquals(flight.getDepartureDate(), flightDto.departureDate());
        Assertions.assertEquals(flight.getArrivalDate(), flightDto.arrivalDate());
        Assertions.assertEquals(flight.getAirline(), flightDto.airline());
    }

}