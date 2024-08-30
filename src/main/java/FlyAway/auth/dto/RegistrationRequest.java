package FlyAway.auth.dto;

import FlyAway.validation.Password;
import FlyAway.validation.PhoneNumber;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegistrationRequest(
        @NotEmpty(message = "Firstname can not be empty")
        @NotBlank(message = "Firstname can not be blank")
        @Size(min = 2, message = "Firstname is too short")
        String firstname,
        @NotEmpty(message = "Lastname can not be empty")
        @NotBlank(message = "Lastname can not be blank")
        @Size(min = 2, message = "Lastname is too short")
        String lastname,
        @NotEmpty(message = "Email can not be empty")
        @NotBlank(message = "Email can not be blank")
        @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                message = "Email should be valid")
        String email,
        @NotEmpty(message = "Password can not be empty")
        @NotBlank(message = "Password can not be blank")
        @Password
        String password,
        @NotEmpty(message = "Phone number can not be empty")
        @NotBlank(message = "Phone number can not be blank")
        @PhoneNumber
        String phoneNumber,
        @NotNull(message = "dateOfBirth is required")
        @Past(message = "Date of brith should be a past date")
        LocalDate dayOfBirth
) {
}
