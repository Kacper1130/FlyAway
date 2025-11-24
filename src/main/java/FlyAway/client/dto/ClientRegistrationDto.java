package FlyAway.client.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClientRegistrationDto(
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
        String password,
        String phoneNumber,
        @NotNull
        @Past(message = "Date of brith should be a past date")
        LocalDate dayOfBirth
) {
}
