package FlyAway.reservation;

import FlyAway.exception.*;
import FlyAway.flight.Flight;
import FlyAway.flight.dao.FlightRepository;
import FlyAway.flight.FlightService;
import FlyAway.flight.aircraft.AircraftService;
import FlyAway.reservation.dao.ReservationRepository;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.ReservationDetailsClientDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationSummaryClientDto;
import FlyAway.security.SecurityUser;
import FlyAway.user.User;
import FlyAway.user.dao.UserRepository;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository clientRepository;
    private final FlightRepository flightRepository;
    private final AircraftService aircraftService;
    private final FlightService flightService;
    private final ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientReservationService.class);

    public ClientReservationService(ReservationRepository reservationRepository, UserRepository userRepository, FlightRepository flightRepository, AircraftService aircraftService, FlightService flightService) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = userRepository;
        this.flightRepository = flightRepository;
        this.aircraftService = aircraftService;
        this.flightService = flightService;
    }

    @Transactional
    public ReservationDto createReservation(CreateReservationDto createReservationDto, Authentication authentication) {
        LOGGER.debug("Creating new reservation");
        var securityUser = (SecurityUser) authentication.getPrincipal();
        User client = (User) securityUser.getUser();
        Flight flight = flightRepository.findById(createReservationDto.flightId())
                .orElseThrow(FlightDoesNotExistException::new);

        if (!flight.getCabinClassPrices().keySet().contains(createReservationDto.cabinClass())) {
            throw new CabinClassDoesNotExistException(createReservationDto.cabinClass());
        }

        var availableSeats = flightService.getAvailableSeats(createReservationDto.flightId(), createReservationDto.cabinClass().toString());
        if (!availableSeats.availableSeats().containsKey(createReservationDto.seatNumber()) ||
                !availableSeats.availableSeats().get(createReservationDto.seatNumber())) {
            throw new UnavailableSeatNumberException(createReservationDto.seatNumber());
        }

        Reservation reservation = Reservation.builder()
                .reservationDate(LocalDateTime.now())
                .price(flight.getCabinClassPrices().get(createReservationDto.cabinClass()))
                .seatNumber(createReservationDto.seatNumber())
                .cabinClass(aircraftService.getCabinClassBySeatNumber(flight.getAircraft(), createReservationDto.seatNumber()))
                .status(ReservationStatus.ACTIVE)
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

    public List<ReservationSummaryClientDto> getActiveReservations(Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        User client = (User) securityUser.getUser();

        if (!clientRepository.existsById(client.getId())) {
            throw new UserDoesNotExistException();
        }

        var reservations = reservationRepository.findActiveByUserId(client.getId())
                .stream()
                .map(reservationMapper::reservationToReservationSummaryClientDto)
                .toList();

        LOGGER.info("Retrieved {} active reservations of user {}", reservations.size(), client.getEmail());
        return reservations;
    }

    public List<ReservationSummaryClientDto> getReservationHistory(Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        User client = (User) securityUser.getUser();

        if (!clientRepository.existsById(client.getId())) {
            throw new UserDoesNotExistException();
        }

        var reservations = reservationRepository.findAllByUserId(client.getId())
                .stream()
                .filter(reservation -> reservation.getStatus() != ReservationStatus.ACTIVE)
                .map(reservationMapper::reservationToReservationSummaryClientDto)
                .toList();

        LOGGER.info("Retrieved reservation history for client {}", client.getEmail());
        return reservations;
    }

    public ReservationDetailsClientDto getReservationDetails(UUID id, Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        User client = (User) securityUser.getUser();

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationDoesNotExistException::new);

        if (!Objects.equals(reservation.getClient().getEmail(), client.getEmail())) {
            throw new AccessDeniedException("You do not have access to this reservation");
        }

        LOGGER.info("{} retrieved details of reservation {}", client.getEmail(), reservation.getId());
        return reservationMapper.reservationToReservationDetailsClientDto(reservation);
    }

    public void cancelOwnReservation(UUID id, Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        User client = (User) securityUser.getUser();

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationDoesNotExistException::new);

        if (!Objects.equals(reservation.getClient().getEmail(), client.getEmail())) {
            throw new AccessDeniedException("You do not have access to this reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        LOGGER.info("{} cancelled reservation {}", client.getEmail(), reservation.getId());
    }
}