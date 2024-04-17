package FlyAway.flight;

import FlyAway.reservation.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String airline;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations;

}
