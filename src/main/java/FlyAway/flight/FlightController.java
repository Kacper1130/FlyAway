package FlyAway.flight;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAll() {
        List<Flight> flights = flightService.getALL();
        return ResponseEntity.ok(flights);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Flight flight) {
        flightService.addFlight(flight);
        return ResponseEntity.created(URI.create("/api/v1/flights/add" + flight.getId())).body(flight);
    }
}
