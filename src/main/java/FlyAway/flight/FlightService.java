package FlyAway.flight;

import FlyAway.exception.CountryDoesNotExistException;
import FlyAway.flight.dto.FlightDto;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    private FlightMapper flightMapper = Mappers.getMapper(FlightMapper.class);

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

    public FlightDto addFlight(FlightDto createFlightDto) {
        LOGGER.debug("Adding new flight");
        if(!createFlightDto.arrivalAirport().getCountry().isEnabled() || createFlightDto.departureAirport().getCountry().isEnabled()) {
            throw new CountryDoesNotExistException();
        }
        Flight createdFlight = flightRepository.save(flightMapper.flightDtoToFlight(createFlightDto));
        LOGGER.info("Created flight with id {}", createdFlight.getId());
        return flightMapper.flightToFlightDto(createdFlight);
    }
}
