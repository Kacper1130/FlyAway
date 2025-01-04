package FlyAway.client.dto;

import FlyAway.validation.PhoneNumber;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClientDto(
        @NotBlank(message = "First name must not be blank")
        @Size(min = 2, message = "First name must be at least 2 characters long")
        String firstname,

        @NotBlank(message = "Last name must not be blank")
        @Size(min = 2, message = "Last name must be at least 2 characters long")
        String lastname,
        @Column(unique = true)
        @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                message = "Email should be valid")
        String email,
        @PhoneNumber(message = "Phone number is not valid")
        String phoneNumber,

        @NotNull(message = "Date of birth must not be null")
        @Past(message = "Date of birth should be a past date")
        LocalDate dayOfBirth
) {
}
