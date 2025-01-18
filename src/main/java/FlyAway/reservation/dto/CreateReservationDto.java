package FlyAway.reservation.dto;

import FlyAway.flight.aircraft.CabinClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateReservationDto (
        @NotNull
        Integer seatNumber,
        @NotNull
        CabinClass cabinClass,
        @NotNull
        UUID flightId
) {

}
