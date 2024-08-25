package FlyAway.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateReservationDto (
        @NotNull
        Long price,
        @NotNull
        Long seatNumber,
        @NotNull
        Long clientId,
        @NotNull
        UUID flightId
) {

}
