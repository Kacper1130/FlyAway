package FlyAway.user;

import FlyAway.reservation.Reservation;
import FlyAway.security.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private LocalDate dayOfBirth;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Reservation> reservations;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

}
