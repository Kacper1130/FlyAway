package FlyAway.reservation;

import FlyAway.flight.Flight;
import FlyAway.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Flight flight;

}
