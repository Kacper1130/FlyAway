package FlyAway.reservation;

import FlyAway.exceptions.FlightDoesNotExistException;
import FlyAway.exceptions.UserDoesNotExistException;
import FlyAway.flight.Flight;
import FlyAway.flight.FlightRepository;
import FlyAway.flight.dto.FlightDto;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.user.User;
import FlyAway.user.UserRepository;
import FlyAway.user.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, FlightRepository flightRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
    }

    public List<DisplayReservationDto> getAll() {
        LOGGER.debug("Retrieving all reservations from repository");
        List<DisplayReservationDto> reservations =  reservationRepository.findAll()
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
        LOGGER.info("Retrieved {} reservations", reservations.size());
        return reservations;
    }

    public ReservationDto addReservation(CreateReservationDto createReservationDto) {
        LOGGER.debug("Adding new reservation");
        Optional<User> user = userRepository.findById(createReservationDto.userId());
        if (user.isPresent()) {
            Optional<Flight> flight = flightRepository.findById(createReservationDto.flightId());
            if (flight.isPresent()) {
                Reservation createdReservation = new Reservation();
                createdReservation.setPrice(createReservationDto.price());
                createdReservation.setSeatNumber(createReservationDto.seatNumber());
                createdReservation.setReservationDate(LocalDateTime.now());
                createdReservation.setUser(user.get());
                createdReservation.setFlight(flight.get());
                reservationRepository.save(createdReservation);
                LOGGER.info("Created reservation with id {}", createdReservation.getId());
                ReservationDto reservationDTO = new ReservationDto(
                        createdReservation.getReservationDate(),
                        createdReservation.getPrice(),
                        createdReservation.getSeatNumber(),
                        new UserDto(
                                createdReservation.getUser().getFirstname(),
                                createdReservation.getUser().getLastname(),
                                createdReservation.getUser().getEmail(),
                                createdReservation.getUser().getPhoneNumber(),
                                createdReservation.getUser().getDayOfBirth()
                        ),
                        new FlightDto(
                                createdReservation.getFlight().getDepartureCity(),
                                createdReservation.getFlight().getArrivalCity(),
                                createdReservation.getFlight().getDepartureDate(),
                                createdReservation.getFlight().getArrivalDate(),
                                createdReservation.getFlight().getAirline()
                        )
                );
                return reservationDTO;
            } else {
                LOGGER.error("Flight with id {} does not exist", createReservationDto.flightId());
                throw new FlightDoesNotExistException("Flight with id: " + createReservationDto.flightId() + " does not exist");
            }
        } else {
            LOGGER.error("User with id {} does not exist", createReservationDto.userId());
            throw new UserDoesNotExistException("User with id: " + createReservationDto.userId() + " does not exist");
        }

    }

}
