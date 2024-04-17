package FlyAway.user.dto;

import FlyAway.reservation.Reservation;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationWithoutUserDto;

import java.time.LocalDate;
import java.util.List;

public record UserReservationDto(
        String firstname,
        String lastname,
        String email,
        String phoneNumber,
        LocalDate dayOfBirth,
        List<ReservationWithoutUserDto> reservations
) {
}
