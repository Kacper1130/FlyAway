package FlyAway.client.dto;

import java.time.LocalDate;

public record ClientDto(
        String firstname,
        String lastname,
        String email,
        String phoneNumber,
        LocalDate dayOfBirth
) {
}
