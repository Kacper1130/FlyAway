package FlyAway.flight;

import FlyAway.flight.dto.FlightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<FlightDto> getAll() {
        LOGGER.debug("Retrieving all flights from repository");
        List<FlightDto> flights = flightRepository.findAll()
                .stream().map(FlightMapper.INSTANCE::flightToFlightDto)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} flights from repository", flights.size());
        return flights;
    }

    public FlightDto addFlight(FlightDto createFlightDto) {
        LOGGER.debug("Adding new flight");
        Flight createdFlight = FlightMapper.INSTANCE.flightDtoToFlight(createFlightDto);
        Flight flight = flightRepository.save(createdFlight);
        LOGGER.info("Created flight with id {}", createdFlight.getId());
        FlightDto createdFlightDto = FlightMapper.INSTANCE.flightToFlightDto(flight);
        return createdFlightDto;
    }
}
