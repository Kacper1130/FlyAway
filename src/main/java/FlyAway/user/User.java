package FlyAway.user;

import FlyAway.reservation.Reservation;
import FlyAway.security.Role;
import FlyAway.validation.Password;
import FlyAway.validation.PhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@SoftDelete(converter = NumericBooleanConverter.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 2)
    private String firstname;
    @NotBlank
    @Size(min = 2)
    private String lastname;
    @Column(unique = true)
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Email should be valid")
    private String email;
    @Password
    private String password;
    @PhoneNumber
    private String phoneNumber;
    @NotNull
    @Past(message = "Date of brith should be a past date")
    private LocalDate dayOfBirth;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

}
