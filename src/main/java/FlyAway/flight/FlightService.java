package FlyAway.flight;

import FlyAway.flight.dto.CreateFlightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAll() {
        LOGGER.debug("Retrieving all flights from repository");
        List<Flight> flights = flightRepository.findAll();
        LOGGER.info("Retrieved {} flights from repository", flights.size());
        return flights;
    }

    public Flight addFlight(CreateFlightDto createFlightDto) {
        LOGGER.debug("Adding new flight");
        Flight createdFlight = new Flight();
        createdFlight.setDepartureCity(createFlightDto.departureCity());
        createdFlight.setArrivalCity(createFlightDto.arrivalCity());
        createdFlight.setDepartureDate(createFlightDto.departureDate());
        createdFlight.setArrivalDate(createFlightDto.arrivalDate());
        createdFlight.setAirline(createFlightDto.airline());
        flightRepository.save(createdFlight);
        LOGGER.info("Created flight with id {}", createdFlight.getId());
        return createdFlight;
    }
}
