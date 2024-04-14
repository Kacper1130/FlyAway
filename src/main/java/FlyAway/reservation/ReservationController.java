package FlyAway.reservation;

import FlyAway.flight.Flight;
import FlyAway.reservation.dto.CreateReservationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getALl() {
        List<Reservation> reservations = reservationService.getAll();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReservation(CreateReservationDto createReservationDto) {
        Reservation reservation = reservationService.addReservation(createReservationDto);
        return ResponseEntity.created(URI.create("/api/v1/reservations/add" + reservation.getId())).body(reservation);
    }
}
