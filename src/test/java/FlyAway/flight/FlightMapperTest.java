package FlyAway.flight;

import FlyAway.flight.dto.FlightDto;
import FlyAway.reservation.Reservation;
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
    void testMapFlightDtoToFlight() {

        FlightDto flightDto = new FlightDto(
                "Cracow",
                "Warsaw",
                LocalDateTime.of(2024, Month.APRIL, 24, 18, 30),
                LocalDateTime.of(2024, Month.APRIL, 24, 19, 30),
                "Ryanair"
        );

        Flight flight = flightMapper.flightDtoToFlight(flightDto);

        assertEquals(flightDto.departureCity(), flight.getDepartureCity());
        assertEquals(flightDto.arrivalCity(), flight.getArrivalCity());
        assertEquals(flightDto.departureDate(), flight.getDepartureDate());
        assertEquals(flightDto.arrivalDate(), flight.getArrivalDate());
        assertEquals(flightDto.airline(), flight.getAirline());
    }

    @Test
    void testMapFlightToFlightDto() {

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

        assertEquals(flight.getDepartureCity(), flightDto.departureCity());
        assertEquals(flight.getArrivalCity(), flightDto.arrivalCity());
        assertEquals(flight.getDepartureDate(), flightDto.departureDate());
        assertEquals(flight.getArrivalDate(), flightDto.arrivalDate());
        assertEquals(flight.getAirline(), flightDto.airline());
    }

}