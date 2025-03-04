package FlyAway.flight.airport;

import FlyAway.flight.airport.dto.AirportDto;
import FlyAway.flight.airport.dto.CreateAirportDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/airports")
@Tag(name = "Airport")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AirportController {

    private final AirportService airportService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AirportController.class);

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public ResponseEntity<List<AirportDto>> getAllAirports() {
        LOGGER.debug("Retrieving all airports");
        List<AirportDto> airports = airportService.getAll();
        LOGGER.info("Retrieved {} airports", airports.size());
        return ResponseEntity.ok(airports);
    }

    @PostMapping
    public ResponseEntity<Airport> createAirport(@Valid @RequestBody CreateAirportDto createAirportDto) {
        LOGGER.debug("Adding new airport {}", createAirportDto);
        Airport airport = airportService.addAirport(createAirportDto);
        LOGGER.info("Created new airport {}", airport.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(airport);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Airport> switchAirportStatus(@PathVariable UUID id) {
        LOGGER.info("Switching status of airport id {}", id);
        Airport airport = airportService.switchAirportStatus(id);
        return ResponseEntity.ok(airport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airport> updateAirport(@PathVariable UUID id, @RequestBody @Valid CreateAirportDto updatedAirport) {
        LOGGER.info("Updating airport with id {}", id);
        Airport udpatedAirport = airportService.updateAirport(id, updatedAirport);
        return ResponseEntity.ok(udpatedAirport);
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<AirportDto>> getAllEnabledAirports() {
        List<AirportDto> activeAirports = airportService.getAllActiveAirports();
        LOGGER.info("Retrieved {} enabled airports", activeAirports.size());
        return ResponseEntity.ok(activeAirports);
    }
}
