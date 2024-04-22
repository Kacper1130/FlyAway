package FlyAway.reservation;

import FlyAway.exceptions.FlightDoesNotExistException;
import FlyAway.exceptions.ReservationDoesNotExistException;
import FlyAway.exceptions.UserDoesNotExistException;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
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

    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@RequestBody CreateReservationDto createReservationDto) {
        LOGGER.debug("Adding new reservation " + createReservationDto);
        try {
            ReservationDto reservationDto = reservationService.addReservation(createReservationDto);
            LOGGER.info("Created new reservation " + reservationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationDto);
        } catch (UserDoesNotExistException e) {
            LOGGER.error("User with id {} does not exist", createReservationDto.userId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given id not found", e);
        } catch (FlightDoesNotExistException e){
            LOGGER.error("Flight with id {} does not exist",createReservationDto.flightId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight with given id not found", e);
        }
    }

    @DeleteMapping("{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable UUID id) {
        LOGGER.debug("Cancelling reservation with id {} ", id);
        try{
            reservationService.cancelReservation(id);
            LOGGER.info("Successfully cancelled reservation");
            return ResponseEntity.ok("Cancelled reservation");
        } catch (ReservationDoesNotExistException e ) {
            LOGGER.error("Reservation with id {} does not exist",id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation with given id not found");
        }
    }
}
