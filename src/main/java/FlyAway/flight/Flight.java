package FlyAway.flight;

import FlyAway.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
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
    private UUID id;
    @NotBlank
    private String departureCity;
    @NotBlank
    private String arrivalCity;
    @NotNull
    private LocalDateTime departureDate;
    @NotNull
    private LocalDateTime arrivalDate;
    @NotBlank
    private String airline;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations;

}
