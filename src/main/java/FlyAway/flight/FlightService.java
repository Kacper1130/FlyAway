package FlyAway.flight;

import FlyAway.flight.dto.FlightDto;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    private FlightMapper flightMapper = Mappers.getMapper(FlightMapper.class);

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<FlightDto> getAll() {
        LOGGER.debug("Retrieving all flights from repository");
        List<FlightDto> flights = flightRepository.findAll()
                .stream().map(flightMapper::flightToFlightDto)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} flights from repository", flights.size());
        return flights;
    }

    public FlightDto addFlight(FlightDto createFlightDto) {
        LOGGER.debug("Adding new flight");
        Flight createdFlight = flightRepository.save(flightMapper.flightDtoToFlight(createFlightDto));
        LOGGER.info("Created flight with id {}", createdFlight.getId());
        return flightMapper.flightToFlightDto(createdFlight);
    }
}
