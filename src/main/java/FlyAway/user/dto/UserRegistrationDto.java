package FlyAway.user.dto;

import java.time.LocalDate;

public record UserRegistrationDto(
        String firstname,
        String lastname,
        String email,
        String password,
        String phoneNumber,
        LocalDate dateOfBirth ) {
}
