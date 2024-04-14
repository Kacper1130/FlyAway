package FlyAway.reservation;

import FlyAway.flight.FlightRepository;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, FlightRepository flightRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Reservation addReservation(CreateReservationDto createReservationDto) {
        Reservation createdReservation = new Reservation();
        createdReservation.setPrice(createReservationDto.price());
        createdReservation.setSeatNumber(createReservationDto.seatNumber());
        createdReservation.setReservationDate(LocalDateTime.now());
        createdReservation.setUser(userRepository.getById(createReservationDto.userId())); //TODO change to findbyid
        createdReservation.setFlight(flightRepository.getById(createReservationDto.flightId())); //TODO same
        reservationRepository.save(createdReservation);
        return createdReservation;
    }

}
