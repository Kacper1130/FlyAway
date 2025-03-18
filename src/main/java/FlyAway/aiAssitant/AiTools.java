package FlyAway.aiAssitant;


import FlyAway.flight.FlightMapper;
import FlyAway.flight.FlightRepository;
import FlyAway.reservation.ReservationRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AiTools {

    private final FlightRepository flightRepository;
    private final ReservationRepository reservationRepository;
    private final FlightMapper flightMapper = Mappers.getMapper(FlightMapper.class);

    public AiTools(FlightRepository flightRepository, ReservationRepository reservationRepository) {
        this.flightRepository = flightRepository;
        this.reservationRepository = reservationRepository;
    }

    @Tool(description = "get available flights")
    public String getAvailableFlights() {
        var flights = flightRepository.findAll().stream().map(flightMapper::flightToFlightBasicDto).toList();

        StringBuilder sb = new StringBuilder();
        sb.append("Available flights:\n");

        for (int i = 0; i < flights.size(); i++) {
            var flight = flights.get(i);
            sb.append(String.format("Flight #%d: %s to %s, Departure: %s, Arrival: %s\n",
                    i+1,
                    flight.departureCity(),
                    flight.arrivalCity(),
                    flight.departureDate(),
                    flight.arrivalDate()));
        }

        return sb.toString();
    }

    @Tool(description = "check reservation status")
    public String checkReservationStatus(String reservationId) {
        var optionalReservation = reservationRepository.findById(UUID.fromString(reservationId));
        if (optionalReservation.get() == null) {
            return "Reservation with given id does not exist";
        }
        return optionalReservation.get().getStatus().toString();
    }

}
