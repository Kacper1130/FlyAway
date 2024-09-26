package FlyAway.reservation;

import FlyAway.flight.Flight;
import FlyAway.client.Client;
import FlyAway.flight.aircraft.CabinClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
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
    private BigDecimal price;
    @NotNull
    private Integer seatNumber;
    private CabinClass cabinClass;
    private boolean cancelled = false;
    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    private Flight flight;

}
