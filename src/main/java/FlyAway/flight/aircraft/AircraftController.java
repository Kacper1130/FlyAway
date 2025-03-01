package FlyAway.flight.aircraft;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aircraft")
@Tag(name = "Aircraft")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AircraftController {

    private final AircraftService aircraftService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AircraftController.class);

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @GetMapping
    public ResponseEntity<List<Aircraft>> getAllAircraft() {
        LOGGER.debug("Retrieving all aircraft");
        List<Aircraft> aircraft = aircraftService.getAllAircraft();
        LOGGER.info("Retrieved {} aircraft", aircraft.size());
        return ResponseEntity.ok(aircraft);
    }

    @PostMapping
    public ResponseEntity<Aircraft> createAircraft(@Valid @RequestBody Aircraft aircraft) {
        LOGGER.debug("Adding new aircraft {}", aircraft);
        Aircraft createdAircraft = aircraftService.addAircraft(aircraft);
        LOGGER.info("Created new aircraft {}", createdAircraft);
        return ResponseEntity.status(HttpStatus.CREATED).body(aircraft);
    }

}
