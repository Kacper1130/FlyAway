package FlyAway.flight;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAll() {
        return flightRepository.findAll();
    }

    public Flight addFlight(Flight flight) {
        Flight createdFlight = flightRepository.save(flight);
        return createdFlight;
    }
}
