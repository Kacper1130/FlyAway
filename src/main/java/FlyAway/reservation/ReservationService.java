package FlyAway.reservation;

import FlyAway.exception.FlightDoesNotExistException;
import FlyAway.exception.ReservationDoesNotExistException;
import FlyAway.exception.UserDoesNotExistException;
import FlyAway.flight.Flight;
import FlyAway.flight.FlightRepository;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.client.Client;
import FlyAway.client.ClientRepository;
import org.mapstruct.factory.Mappers;
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
    private final ClientRepository userRepository;
    private final FlightRepository flightRepository;
    private final ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService(ReservationRepository reservationRepository, ClientRepository userRepository, FlightRepository flightRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
    }

    public List<DisplayReservationDto> getAll() {
        LOGGER.debug("Retrieving all reservations from repository");
        List<DisplayReservationDto> reservations = reservationRepository.findAll()
                .stream().map(reservationMapper::reservationToDisplayReservationDto)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} reservations", reservations.size());
        return reservations;
    }

    public ReservationDto addReservation(CreateReservationDto createReservationDto) {
        LOGGER.debug("Adding new reservation");
        Optional<Client> optionalUser = userRepository.findActiveById(createReservationDto.clientId());
        if (optionalUser.isPresent()) {
            Client client = optionalUser.get();
            Optional<Flight> optionalFlight = flightRepository.findById(createReservationDto.flightId());
            if (optionalFlight.isPresent()) {
                Flight flight = optionalFlight.get();
                Reservation createdReservation = new Reservation();
                createdReservation.setPrice(createReservationDto.price());
                createdReservation.setSeatNumber(createReservationDto.seatNumber());
                createdReservation.setReservationDate(LocalDateTime.now());
                createdReservation.setClient(client);
                createdReservation.setFlight(flight);

                List<Reservation> userReservations = client.getReservations();
                userReservations.add(createdReservation);
                client.setReservations(userReservations);

                List<Reservation> flightReservations = flight.getReservations();
                flightReservations.add(createdReservation);
                flight.setReservations(flightReservations);

                reservationRepository.save(createdReservation);
                LOGGER.info("Created reservation with id {}", createdReservation.getId());

                ReservationDto reservationDTO = reservationMapper.reservationToReservationDto(createdReservation);
                LOGGER.debug("Mapped to ReservationDto");
                return reservationDTO;
            } else {
                LOGGER.error("Flight with id {} does not exist", createReservationDto.flightId());
                throw new FlightDoesNotExistException("Flight with id: " + createReservationDto.flightId() + " does not exist");
            }
        } else {
            LOGGER.error("User with id {} does not exist", createReservationDto.clientId());
            throw new UserDoesNotExistException("User with id: " + createReservationDto.clientId() + " does not exist");
        }

    }

    public ReservationDto getReservation(UUID id) {
        LOGGER.debug("Retrieving reservation with id {}",id);
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        return optionalReservation.map(
                r -> {
                    LOGGER.info("Successfully retrieved reservation with id {}",id);
                    return reservationMapper.reservationToReservationDto(r);
                }
        ).orElseThrow(() -> {
            LOGGER.error("Reservation with id {} does not exist",id);
            throw new ReservationDoesNotExistException(id);
        });
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