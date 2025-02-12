package FlyAway.reservation;

import FlyAway.client.Client;
import FlyAway.client.ClientRepository;
import FlyAway.email.EmailService;
import FlyAway.exception.*;
import FlyAway.flight.Flight;
import FlyAway.flight.FlightRepository;
import FlyAway.flight.FlightService;
import FlyAway.flight.aircraft.AircraftService;
import FlyAway.payment.PaymentService;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationPaymentResponseDto;
import FlyAway.security.SecurityUser;
import com.stripe.exception.StripeException;
import jakarta.mail.MessagingException;
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
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final FlightRepository flightRepository;
    private final EmailService emailService;
    private final AircraftService aircraftService;
    private final FlightService flightService;
    private final PaymentService paymentService;
    private final ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService(ReservationRepository reservationRepository, ClientRepository userRepository, FlightRepository flightRepository, EmailService emailService, AircraftService aircraftService, FlightService flightService, PaymentService paymentService) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = userRepository;
        this.flightRepository = flightRepository;
        this.emailService = emailService;
        this.aircraftService = aircraftService;
        this.flightService = flightService;
        this.paymentService = paymentService;
    }

    public List<DisplayReservationDto> getAll() {
        LOGGER.debug("Retrieving all reservations from repository");
        List<DisplayReservationDto> reservations = reservationRepository.findAll()
                .stream().map(reservationMapper::reservationToDisplayReservationDto)
                .toList();
        LOGGER.info("Retrieved {} reservations", reservations.size());
        return reservations;
    }

    public ReservationPaymentResponseDto createReservation(CreateReservationDto createReservationDto, Authentication authentication) {
        LOGGER.debug("Creating new reservation");
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();
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
                .status(ReservationStatus.PENDING)
                .client(client)
                .flight(flight)
                .build();

        reservationRepository.save(reservation);

        LOGGER.info("Created new pending reservation for client {} and flight {}",
                client.getId(),
                createReservationDto.flightId()
        );

        try {
            String paymentUrl = paymentService.createSession(reservation);
            return new ReservationPaymentResponseDto(
                    reservationMapper.reservationToReservationDto(reservation),
                    paymentUrl
            );
        } catch (StripeException e) {
            reservation.setStatus(ReservationStatus.FAILED);
            reservationRepository.save(reservation);
            LOGGER.error("Stripe error for reservation {}: {}", reservation.getId(), e.getMessage());
            throw new RuntimeException("Payment error");
        }
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
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
            LOGGER.info("Successfully cancelled reservation with id {}", id);
        } else {
            LOGGER.error("Reservation with id {} does not exist", id);
            throw new ReservationDoesNotExistException(id);
        }
    }

    public void handlePaymentCompleted(String reservationId) throws MessagingException {
        Reservation reservation = reservationRepository.findById(UUID.fromString(reservationId))
                .orElseThrow(ReservationDoesNotExistException::new);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservationRepository.save(reservation);
        LOGGER.info("Reservation {} status: ACTIVE", reservation.getId());
        emailService.sendReservationConfirmationEmail(
                reservation.getClient().getEmail(),
                reservation.getClient().getFirstname(),
                reservation.getClient().getLastname(),
                reservation.getFlight(),
                reservation.getCabinClass().toString(),
                reservation.getSeatNumber()
        );
    }

    public void handlePaymentExpired(String reservationId) {
        Reservation reservation = reservationRepository.findById(UUID.fromString(reservationId))
                .orElseThrow(ReservationDoesNotExistException::new);
        reservation.setStatus(ReservationStatus.EXPIRED);
        reservationRepository.save(reservation);
        LOGGER.info("Reservation {} status: EXPIRED", reservation.getId());
    }

    public List<ReservationDto> getOwnReservations(Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = clientRepository.findByIdWithReservations(securityUser.getUser().getId())
                .orElseThrow(UserDoesNotExistException::new);
        var reservations = client
                .getReservations()
                .stream()
                .map(reservationMapper::reservationToReservationDto)
                .toList();
        LOGGER.info("Retrieved {} reservation of user {}", reservations.size(), client.getEmail());
        return reservations;
    }

    public ReservationDto getReservationDetails(UUID id, Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationDoesNotExistException::new);

        if (!Objects.equals(reservation.getClient().getEmail(), client.getEmail())) {
            throw new AccessDeniedException("You do not have access to this reservation");
        }

        LOGGER.info("{} retrieved details of reservation {}", client.getEmail(), reservation.getId());
        return reservationMapper.reservationToReservationDto(reservation);
    }

    public void cancelOwnReservation(UUID id, Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();

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