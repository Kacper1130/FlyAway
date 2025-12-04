package FlyAway.user.dao;

import FlyAway.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Document(collection = "users")
public class UserDocument {

    @Id
    private UUID id;
    @NotBlank
    @Size(min = 2)
    private String firstname;
    @NotBlank
    @Size(min = 2)
    private String lastname;
    @Indexed(unique = true)
    @Email
    private String email;
    private String password;
    private String phoneNumber;
    private boolean enabled;

    private Role role;

    // --- CLIENT ---
    @Past
    private LocalDate dayOfBirth;

    private Boolean deleted;

    // --- EMPLOYEE ---
    private LocalDate hireDate;
    private LocalDateTime lastLogin;
}