package FlyAway.reservation;

import FlyAway.exceptions.FlightDoesNotExistException;
import FlyAway.exceptions.UserDoesNotExistException;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<DisplayReservationDto>> getALl() {
        List<DisplayReservationDto> reservations = reservationService.getAll();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@RequestBody CreateReservationDto createReservationDto) {
        try {
            Reservation reservation = reservationService.addReservation(createReservationDto);
            return ResponseEntity.created(URI.create("/api/v1/reservations/add" + reservation.getId())).body(reservation);
        } catch (UserDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given id not found", e);
        } catch (FlightDoesNotExistException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight with given id not found", e);
        }

    }
}
