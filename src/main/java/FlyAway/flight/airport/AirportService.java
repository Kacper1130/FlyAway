package FlyAway.flight.airport;

import FlyAway.exception.AirportDoesNotExistException;
import FlyAway.exception.CountryDoesNotExistException;
import FlyAway.flight.airport.dto.AirportDto;
import FlyAway.flight.airport.dto.CreateAirportDto;
import FlyAway.flight.country.Country;
import FlyAway.flight.country.CountryRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AirportService {

    private final AirportRepository airportRepository;
    private final CountryRepository countryRepository;
    private final AirportMapper airportMapper = Mappers.getMapper(AirportMapper.class);

    private static final Logger LOGGER = LoggerFactory.getLogger(AirportService.class);

    public AirportService(AirportRepository airportRepository, CountryRepository countryRepository) {
        this.airportRepository = airportRepository;
        this.countryRepository = countryRepository;
    }

    public List<AirportDto> getAll() {
        LOGGER.debug("Retrieving all airports from repository");
        List<AirportDto> airports = airportRepository.findAll()
                        .stream().map(airportMapper::airportToAirportDto).toList();
        LOGGER.info("Retrieved {} airports from repository", airports.size());
        return airports;
    }

    public AirportDto addAirport(CreateAirportDto createAirportDto) {
        LOGGER.debug("Adding new airport");
        Country country = countryRepository.findByName(createAirportDto.country())
                .orElseThrow(() -> new CountryDoesNotExistException(createAirportDto.country()));
        Airport airport = Airport.builder()
                .name(createAirportDto.name())
                .IATACode(createAirportDto.IATACode())
                .city(createAirportDto.city())
                .enabled(createAirportDto.enabled())
                .country(country)
                .build();
        airportRepository.save(airport);
        LOGGER.info("Created flight with id {}", airport.getId());
        return airportMapper.airportToAirportDto(airport);
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

    public void deleteAirport(UUID id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(AirportDoesNotExistException::new);
        airportRepository.deleteById(id);
        LOGGER.info("Deleted airport {} in {}", airport.getName(), airport.getCountry().getName());
    }
}
