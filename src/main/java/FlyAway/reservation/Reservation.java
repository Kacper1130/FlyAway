package FlyAway.reservation;

import FlyAway.flight.Flight;
import FlyAway.client.Client;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private LocalDateTime reservationDate;
    @NotNull
    private Long price;
    @NotNull
    private Long seatNumber;
    private boolean cancelled = false;
    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    private Flight flight;

}
