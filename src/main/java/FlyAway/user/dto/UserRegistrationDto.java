package FlyAway.user.dto;

import FlyAway.validation.Password;
import FlyAway.validation.PhoneNumber;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRegistrationDto(
        @NotBlank
        @Size(min = 2)
        String firstname,
        @NotBlank
        @Size(min = 2)
        String lastname,
        @Column(unique = true)
        @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                message = "Email should be valid")
        String email,
        @Password
        String password,
        @PhoneNumber
        String phoneNumber,
        @NotNull
        @Past(message = "Date of brith should be a past date")
        LocalDate dayOfBirth
) {
}
