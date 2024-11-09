package FlyAway.flight.airport;

import FlyAway.flight.airport.dto.AirportDto;
import FlyAway.flight.airport.dto.CreateAirportDto;
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
@RequestMapping("/api/v1/airports")
@Tag(name = "Airport")
public class AirportController {

    private final AirportService airportService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AirportController.class);

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public ResponseEntity<List<AirportDto>> getAll() {
        LOGGER.debug("Retrieving all airports");
        List<AirportDto> airports = airportService.getAll();
        LOGGER.info("Retrieved {} airports", airports.size());
        return ResponseEntity.ok(airports);
    }

    @PostMapping("/add")
    public ResponseEntity<AirportDto> add(@Valid @RequestBody CreateAirportDto createAirportDto) {
        LOGGER.debug("Adding new airport {}", createAirportDto);
        AirportDto airport = airportService.addAirport(createAirportDto);
        LOGGER.info("Created new airport {}", airport);
        return ResponseEntity.status(HttpStatus.CREATED).body(airport);
    }

    @PatchMapping("/{id}")
    public Airport switchAirportStatus(@PathVariable UUID id) {
        LOGGER.info("Switching status of airport id {}", id);
        return airportService.switchAirportStatus(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable UUID id) {
        LOGGER.info("Deleting airport with id {}", id);
        airportService.deleteAirport(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
