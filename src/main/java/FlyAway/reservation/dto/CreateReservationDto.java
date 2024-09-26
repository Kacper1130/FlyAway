package FlyAway.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateReservationDto (
        @NotNull
        BigDecimal price,
        @NotNull
        Integer seatNumber,
        @NotNull
        Long clientId,
        @NotNull
        UUID flightId
) {

}
