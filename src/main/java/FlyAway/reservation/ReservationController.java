package FlyAway.reservation;

import FlyAway.reservation.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @GetMapping("")
    public ResponseEntity<List<ReservationSummaryClientDto>> getOwnReservations(Authentication authentication) {
        List<ReservationSummaryClientDto> reservations = reservationService.getOwnReservations(authentication);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDetailsClientDto> getReservationDetails(@PathVariable UUID id, Authentication authentication) {
        ReservationDetailsClientDto reservation = reservationService.getReservationDetails(id, authentication);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelOwnReservation(@PathVariable UUID id, Authentication authentication) {
        reservationService.cancelOwnReservation(id, authentication);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Reservation has been successfully cancelled");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<List<DisplayReservationDto>> getAllReservations() {
        LOGGER.debug("Retrieving all reservations");
        List<DisplayReservationDto> reservations = reservationService.getAll();
        LOGGER.info("Retrieved {} reservations", reservations.size());
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/create")
    public ResponseEntity<ReservationPaymentResponseDto> createReservation(
            @Valid @RequestBody CreateReservationDto createReservationDto,
            Authentication authentication
    ) {
        LOGGER.debug("Creating new reservation {}", createReservationDto);
        ReservationPaymentResponseDto reservationDto = reservationService.createReservation(createReservationDto, authentication);
        LOGGER.info("Pending reservation created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationDto);
    }

    @DeleteMapping("{id}/cancel")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<String> cancelReservation(@PathVariable UUID id) {
        LOGGER.debug("Cancelling reservation with id {} ", id);
        reservationService.cancelReservation(id);
        LOGGER.info("Successfully cancelled reservation");
        return ResponseEntity.ok("Cancelled reservation");
    }

}
