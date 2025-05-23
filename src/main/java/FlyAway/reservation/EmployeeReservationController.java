package FlyAway.reservation;


import FlyAway.common.PageResponse;
import FlyAway.reservation.dto.ReservationDetailsEmployeeDto;
import FlyAway.reservation.dto.ReservationSummaryEmployeeDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employee/reservations")
@Tag(name = "EmployeeReservation")
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public class EmployeeReservationController {

    private final EmployeeReservationService employeeReservationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeReservationController.class);

    public EmployeeReservationController(EmployeeReservationService employeeReservationService) {
        this.employeeReservationService = employeeReservationService;
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<ReservationSummaryEmployeeDto>> getReservations(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        LOGGER.debug("Retrieving all reservations");
        PageResponse<ReservationSummaryEmployeeDto> reservations = employeeReservationService.getReservations(page, size);
        LOGGER.info("Retrieved {} reservations", reservations.getTotalElements());
        return ResponseEntity.ok().body(reservations);
    }

    @GetMapping("/search")
    public ResponseEntity<ReservationSummaryEmployeeDto> searchReservationById(@RequestParam UUID id) {
        LOGGER.debug("Searching reservation with id {}", id);
        ReservationSummaryEmployeeDto reservation = employeeReservationService.getReservationSummary(id);
        LOGGER.info("Found reservation with id {}", id);
        return ResponseEntity.ok().body(reservation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDetailsEmployeeDto> getReservationDetails(@PathVariable UUID id) {
        LOGGER.debug("Fetching reservation with id {} ", id);
        ReservationDetailsEmployeeDto reservationDetails = employeeReservationService.getReservationDetails(id);
        LOGGER.info("Successfully fetched reservation");
        return ResponseEntity.ok().body(reservationDetails);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> cancelReservation(@PathVariable UUID id) {
        LOGGER.debug("Cancelling reservation with id {} ", id);
        employeeReservationService.cancelReservation(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Reservation has been successfully cancelled");
        LOGGER.info("Successfully cancelled reservation");
        return ResponseEntity.ok(response);
    }

}
