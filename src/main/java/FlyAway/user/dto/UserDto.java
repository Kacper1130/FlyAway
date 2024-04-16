package FlyAway.user.dto;

import java.time.LocalDate;

public record UserDto (
        String firstname,
        String lastname,
        String email,
        String phoneNumber,
        LocalDate dayOfBirth
) {
}
