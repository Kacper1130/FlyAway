package FlyAway.flight.aircraft;

import FlyAway.flight.airport.AirportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AircraftService {

    private final AircraftRepository aircraftRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(AirportService.class);

    public AircraftService(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public List<Aircraft> getAllAircraft() {
        LOGGER.debug("Retrieving all aircraft from repository");
        List<Aircraft> aircraft = aircraftRepository.findAll();
        LOGGER.info("Retrieved {} aircraft from repository", aircraft.size());
        return aircraft;
    }

    public Aircraft addAircraft(Aircraft aircraft) {
        LOGGER.debug("Adding new aircraft");
        Aircraft createdAircraft = aircraftRepository.save(aircraft);
        LOGGER.info("Created aircraft with id {}", createdAircraft.getId());
        return createdAircraft;
    }
}
