package FlyAway.reservation.dto;

import java.util.UUID;

public record CreateReservationDto (
        Long price,
        Long seatNumber,
        Long userId,
        UUID flightId
) {

}
