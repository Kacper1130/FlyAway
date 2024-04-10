package FlyAway.flight;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getALL() {
        return flightRepository.findAll();
    }

    public void addFlight(Flight flight) {
        flightRepository.save(flight);
    }
}
