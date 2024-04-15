package FlyAway.flight;

import FlyAway.flight.dto.CreateFlightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAll() {
        LOGGER.debug("Retrieving all flights");
        List<Flight> flights = flightService.getAll();
        LOGGER.info("Retrieved {} flights", flights.size());
        return ResponseEntity.ok(flights);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody CreateFlightDto createFlightDto) {
        LOGGER.debug("Adding new flight " + createFlightDto);
        Flight flight = flightService.addFlight(createFlightDto);       //TODO add catch try
        LOGGER.info("Added new flight with id {}",flight.getId());
        return ResponseEntity.created(URI.create("/api/v1/flights/add" + flight.getId())).body(flight);
    }
}
