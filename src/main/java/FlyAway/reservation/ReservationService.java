package FlyAway.reservation;

import FlyAway.exception.*;
import FlyAway.flight.Flight;
import FlyAway.flight.FlightRepository;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.client.Client;
import FlyAway.client.ClientRepository;
import FlyAway.security.SecurityUser;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final FlightRepository flightRepository;
    private final ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService(ReservationRepository reservationRepository, ClientRepository userRepository, FlightRepository flightRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = userRepository;
        this.flightRepository = flightRepository;
    }

    public List<DisplayReservationDto> getAll() {
        LOGGER.debug("Retrieving all reservations from repository");
        List<DisplayReservationDto> reservations = reservationRepository.findAll()
                .stream().map(reservationMapper::reservationToDisplayReservationDto)
                .toList();
        LOGGER.info("Retrieved {} reservations", reservations.size());
        return reservations;
    }

    public ReservationDto createReservation(CreateReservationDto createReservationDto, Authentication authentication) {
        LOGGER.debug("Creating new reservation");
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();
        Flight flight = flightRepository.findById(createReservationDto.flightId())
                .orElseThrow(FlightDoesNotExistException::new);

        if (!flight.getCabinClassPrices().keySet().contains(createReservationDto.cabinClass())) {
            throw new CabinClassDoesNotExistException(createReservationDto.cabinClass());
        }

        Reservation reservation = Reservation.builder()
                .reservationDate(LocalDateTime.now())
                .price(flight.getCabinClassPrices().get(createReservationDto.cabinClass()))
                .seatNumber(createReservationDto.seatNumber())
                .cabinClass(createReservationDto.cabinClass())
                .client(client)
                .flight(flight)
                .build();
        reservationRepository.save(reservation);
        LOGGER.info("Created new reservation for client {} and flight {}",
                client.getId(),
                createReservationDto.flightId()
        );
        return reservationMapper.reservationToReservationDto(reservation);
    }

    public ReservationDto getReservation(UUID id) {
        LOGGER.debug("Retrieving reservation with id {}", id);
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        return optionalReservation.map(
                r -> {
                    LOGGER.info("Successfully retrieved reservation with id {}", id);
                    return reservationMapper.reservationToReservationDto(r);
                }
        ).orElseThrow(() -> {
            LOGGER.error("Reservation with id {} does not exist", id);
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
            LOGGER.info("Successfully cancelled reservation with id {}", id);
        } else {
            LOGGER.error("Reservation with id {} does not exist", id);
            throw new ReservationDoesNotExistException(id);
        }
    }


}