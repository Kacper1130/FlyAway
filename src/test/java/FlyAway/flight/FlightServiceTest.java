package FlyAway.flight;

import FlyAway.flight.dto.FlightDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class FlightServiceTest {

    @InjectMocks
    FlightService flightService;

    @Mock
    FlightRepository flightRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllWhenFlightExist() {
        Flight flight1 = new Flight(
                UUID.randomUUID(),
                "Cracow",
                "Warsaw",
                LocalDateTime.of(2024, Month.APRIL, 23, 18, 30),
                LocalDateTime.of(2024, Month.APRIL, 23, 19, 30),
                "LOT",
                null
        );

        Flight flight2 = new Flight(
                UUID.randomUUID(),
                "Cracow",
                "London",
                LocalDateTime.of(2024, Month.APRIL, 23, 18, 30),
                LocalDateTime.of(2024, Month.APRIL, 23, 21, 15),
                "LOT",
                null
        );
        List<Flight> mockFlights = new ArrayList<>();
        mockFlights.add(flight1);
        mockFlights.add(flight2);

        when(flightRepository.findAll()).thenReturn(mockFlights);

        List<FlightDto> flights = flightService.getAll();

        assertEquals(mockFlights.size(), flights.size());
        assertEquals(mockFlights.get(0).getDepartureCity(), flights.get(0).departureCity());
        assertEquals(mockFlights.get(1).getAirline(), flights.get(1).airline());
        verify(flightRepository, times(1)).findAll();

    }

    @Test
    void testGetAllWhenNoneExist() {

        when(flightRepository.findAll()).thenReturn(new ArrayList<>());

        List<FlightDto> flights = flightService.getAll();

        assertEquals(0, flights.size());
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void testAddFlight() {
        FlightDto flightDto = new FlightDto(
                "Cracow",
                "Warsaw",
                LocalDateTime.of(2024,Month.APRIL,24,12,30),
                LocalDateTime.of(2024,Month.APRIL,24,13,30),
                "Wizz Air"
        );

        Flight flight = new Flight(
                UUID.randomUUID(),
                "Cracow",
                "Warsaw",
                LocalDateTime.of(2024,Month.APRIL,24,12,30),
                LocalDateTime.of(2024,Month.APRIL,24,13,30),
                "Wizz Air",
                new ArrayList<>()
        );

        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        FlightDto actualFlightDto = flightService.addFlight(flightDto);

        assertNotNull(actualFlightDto);
        assertEquals(flightDto.departureCity(), actualFlightDto.departureCity());
        assertEquals(flightDto.airline(), actualFlightDto.airline());
        verify(flightRepository,times(1)).save(any());
    }


}