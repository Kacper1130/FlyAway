package FlyAway.reservation;


import FlyAway.reservation.dto.DisplayReservationDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employee/reservations")
@Tag(name = "EmployeeReservationController")
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public class EmployeeReservationController {

    private final EmployeeReservationService employeeReservationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeReservationController.class);

    public EmployeeReservationController(EmployeeReservationService employeeReservationService) {
        this.employeeReservationService = employeeReservationService;
    }

    @GetMapping("")
    public ResponseEntity<List<DisplayReservationDto>> getReservations() {
        LOGGER.debug("Retrieving all reservations");
        List<DisplayReservationDto> reservations = employeeReservationService.getReservations();
        LOGGER.info("Retrieved {} reservations", reservations.size());
        return ResponseEntity.ok().body(reservations);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable UUID id) {
        LOGGER.debug("Cancelling reservation with id {} ", id);
        employeeReservationService.cancelReservation(id);
        LOGGER.info("Successfully cancelled reservation");
        return ResponseEntity.ok("Cancelled reservation");
    }

}
