package FlyAway.flight;

import FlyAway.flight.dto.CreateFlightDto;
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

    public Flight addFlight(CreateFlightDto createFlightDto) {
        Flight createdFlight = new Flight();
        createdFlight.setDepartureCity(createFlightDto.departureCity());
        createdFlight.setArrivalCity(createFlightDto.arrivalCity());
        createdFlight.setDepartureDate(createFlightDto.departureDate());
        createdFlight.setArrivalDate(createFlightDto.arrivalDate());
        createdFlight.setAirline(createFlightDto.airline());
        flightRepository.save(createdFlight);
        return createdFlight;
    }
}
