package FlyAway.flight;

import FlyAway.flight.dto.FlightDetailsDto;
import FlyAway.flight.dto.FlightDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flights")
@Tag(name = "Flight")
public class FlightController {

    private final FlightService flightService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<FlightDto>> getAll() {
        LOGGER.debug("Retrieving all flights");
        List<FlightDto> flights = flightService.getAll();
        LOGGER.info("Retrieved {} flights", flights.size());
        return ResponseEntity.ok(flights);
    }

    @PostMapping("/add")
    public ResponseEntity<FlightDto> add(@Valid @RequestBody FlightDto createFlightDto) {
        LOGGER.debug("Adding new flight {} ", createFlightDto);
        FlightDto flight = flightService.addFlight(createFlightDto);
        LOGGER.info("Created new flight {}", flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(flight);
    }

    @GetMapping("/full")
    public ResponseEntity<List<FlightDetailsDto>> getAllFullFlights() {
        LOGGER.debug("Retrieving all full flight entities for employees");
        List<FlightDetailsDto> flights = flightService.getAllFlightsWithId();
        LOGGER.info("Retrieved {} full flight entities", flights.size());
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/full/{id}")
    public ResponseEntity<FlightDetailsDto> getFlightDetails(@PathVariable UUID id) {
        FlightDetailsDto flight = flightService.getFlightDetails(id);
        return ResponseEntity.ok(flight);
    }
}
