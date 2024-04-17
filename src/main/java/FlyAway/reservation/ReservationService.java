package FlyAway.reservation;

import FlyAway.exceptions.FlightDoesNotExistException;
import FlyAway.exceptions.ReservationDoesNotExistException;
import FlyAway.exceptions.UserDoesNotExistException;
import FlyAway.flight.Flight;
import FlyAway.flight.FlightRepository;
import FlyAway.flight.FlightService;
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
import java.util.UUID;
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
        List<DisplayReservationDto> reservations = reservationRepository.findAll()
                .stream().map(
                        r -> new DisplayReservationDto(
                                r.getId(),
                                r.getReservationDate(),
                                r.getPrice(),
                                r.getSeatNumber(),
                                r.getCancelled(),
                                r.getUser().getId(),
                                r.getFlight().getId()
                        )
                ).collect(Collectors.toList());
        LOGGER.info("Retrieved {} reservations", reservations.size());
        return reservations;
    }

    public ReservationDto addReservation(CreateReservationDto createReservationDto) {
        LOGGER.debug("Adding new reservation");
        Optional<User> optionalUserser = userRepository.findById(createReservationDto.userId());
        if (optionalUserser.isPresent()) {
            User user = optionalUserser.get();
            Optional<Flight> optionalFlight = flightRepository.findById(createReservationDto.flightId());
            if (optionalFlight.isPresent()) {
                Flight flight = optionalFlight.get();
                Reservation createdReservation = new Reservation();
                createdReservation.setPrice(createReservationDto.price());
                createdReservation.setSeatNumber(createReservationDto.seatNumber());
                createdReservation.setReservationDate(LocalDateTime.now());
                createdReservation.setUser(user);
                createdReservation.setFlight(flight);

                List<Reservation> userReservations = user.getReservations();
                userReservations.add(createdReservation);
                user.setReservations(userReservations);

                List<Reservation> flightReservations = flight.getReservations();
                flightReservations.add(createdReservation);
                flight.setReservations(flightReservations);

                reservationRepository.save(createdReservation);
                LOGGER.info("Created reservation with id {}", createdReservation.getId());
                ReservationDto reservationDTO = new ReservationDto(   //TODO add to mapper class
                        createdReservation.getReservationDate(),
                        createdReservation.getPrice(),
                        createdReservation.getSeatNumber(),
                        createdReservation.getCancelled(),
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
                LOGGER.debug("Mapped to ReservationDto");
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

    public void cancelReservation(UUID id) {
        LOGGER.debug("Cancelling reservation with id {}", id);
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setCancelled(true);
            reservationRepository.save(reservation);
            LOGGER.info("Successfully cancelled reservation with id {}",id);
        } else {
            LOGGER.error("Reservation with id {} does not exist", id);
            throw new ReservationDoesNotExistException(id);
        }
    }


}
