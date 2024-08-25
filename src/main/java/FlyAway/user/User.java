package FlyAway.user;

import FlyAway.role.Role;
import FlyAway.validation.Password;
import FlyAway.validation.PhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
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
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
