package FlyAway.client;

import FlyAway.reservation.Reservation;
import FlyAway.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends User {

    @NotNull
    @Past(message = "Date of brith should be a past date")
    private LocalDate dayOfBirth;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "client")
    private List<Reservation> reservations;
    //@Column(name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted;

}
