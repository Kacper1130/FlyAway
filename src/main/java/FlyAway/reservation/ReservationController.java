package FlyAway.reservation;

import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationPaymentResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@Tag(name = "Reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<DisplayReservationDto>> getALl() {
        LOGGER.debug("Retrieving all reservations");
        List<DisplayReservationDto> reservations = reservationService.getAll();
        LOGGER.info("Retrieved {} reservations", reservations.size());
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/create")
    public ResponseEntity<ReservationPaymentResponseDto> createReservation(
            @Valid @RequestBody CreateReservationDto createReservationDto,
            Authentication authentication
    ) throws MessagingException {
        LOGGER.debug("Adding new reservation {}", createReservationDto);
        ReservationPaymentResponseDto reservationDto = reservationService.createReservation(createReservationDto, authentication);
        LOGGER.info("Created new reservation {}", reservationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationDto);
    }

    @DeleteMapping("{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable UUID id) {
        LOGGER.debug("Cancelling reservation with id {} ", id);
        reservationService.cancelReservation(id);
        LOGGER.info("Successfully cancelled reservation");
        return ResponseEntity.ok("Cancelled reservation");

    }
}
