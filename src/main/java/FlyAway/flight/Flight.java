package FlyAway.flight;

import FlyAway.flight.aircraft.Aircraft;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.airport.Airport;
import FlyAway.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    private UUID id;
    @ManyToOne
    @NotNull(message = "departureAirport can not be null")
    private Airport departureAirport;
    @ManyToOne
    @NotNull(message = "arrivalAirport can not be null")
    private Airport arrivalAirport;
    @NotNull
    private LocalDateTime departureDate;
    @NotNull
    private LocalDateTime arrivalDate;
    @ManyToOne
    @NotNull(message = "aircraft can not be null")
    private Aircraft aircraft;
    @ElementCollection
    @NotNull(message = "cabinClassPrices can not be null")
    private Map<CabinClass, BigDecimal> cabinClassPrices;
    //@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OneToMany
    private List<Reservation> reservations;

}
