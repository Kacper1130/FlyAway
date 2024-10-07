package FlyAway.flight.airport;

import FlyAway.exception.AirportDoesNotExistException;
import FlyAway.flight.airport.dto.CreateAirportDto;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AirportService {

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper = Mappers.getMapper(AirportMapper.class);

    private static final Logger LOGGER = LoggerFactory.getLogger(AirportService.class);

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> getAll() {
        LOGGER.debug("Retrieving all airports from repository");
        List<Airport> airports = airportRepository.findAll();
        LOGGER.info("Retrieved {} airports from repository", airports.size());
        return airports;
    }

    public Airport addFlight(CreateAirportDto createAirportDto) {
        LOGGER.debug("Adding new airport");
        Airport createdFlight = airportRepository.save(airportMapper.createAirportDtoToAirport(createAirportDto));
        LOGGER.info("Created flight with id {}", createdFlight.getId());
        return createdFlight;
    }

    public Airport switchAirportStatus(UUID id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(AirportDoesNotExistException::new);
        LOGGER.info("Current status of {} - {},", airport.getName(), airport.isEnabled());
        airport.setEnabled(!airport.isEnabled());
        LOGGER.info("Changed status of {} to {}", airport.getName(), airport.isEnabled());
        airportRepository.save(airport);
        return airport;
    }
}
