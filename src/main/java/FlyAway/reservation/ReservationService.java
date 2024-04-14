package FlyAway.reservation;

import FlyAway.exceptions.FlightDoesNotExistException;
import FlyAway.exceptions.UserDoesNotExistException;
import FlyAway.flight.Flight;
import FlyAway.flight.FlightRepository;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.user.User;
import FlyAway.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<DisplayReservationDto> getAll() {
        return reservationRepository.findAll()
                .stream().map(
                        r -> new DisplayReservationDto(
                                r.getId(),
                                r.getReservationDate(),
                                r.getPrice(),
                                r.getSeatNumber(),
                                r.getUser().getId(),
                                r.getFlight().getId()
                        )
                ).collect(Collectors.toList());
    }

    public Reservation addReservation(CreateReservationDto createReservationDto) {
        Optional<User> user = userRepository.findById(createReservationDto.userId());
        if (user.isPresent()){
            Optional<Flight> flight = flightRepository.findById(createReservationDto.flightId());
            if (flight.isPresent()) {
                Reservation createdReservation = new Reservation();
                createdReservation.setPrice(createReservationDto.price());
                createdReservation.setSeatNumber(createReservationDto.seatNumber());
                createdReservation.setReservationDate(LocalDateTime.now());
                createdReservation.setUser(user.get());
                createdReservation.setFlight(flight.get());
                reservationRepository.save(createdReservation);
                return createdReservation;
            } else {
                throw new FlightDoesNotExistException("Flight with id: " + createReservationDto.flightId() + " does not exist");
            }
        } else {
            throw new UserDoesNotExistException("User with id: " + createReservationDto.userId() + " does not exist");
        }

    }

}
