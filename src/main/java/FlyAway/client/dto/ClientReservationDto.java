package FlyAway.client.dto;

import FlyAway.reservation.dto.ReservationWithoutUserDto;

import java.time.LocalDate;
import java.util.List;

public record ClientReservationDto(
        String firstname,
        String lastname,
        String email,
        String phoneNumber,
        LocalDate dayOfBirth,
        List<ReservationWithoutUserDto> reservations
) {
}
