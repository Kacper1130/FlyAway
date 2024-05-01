package FlyAway.flight.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FlightDto(
        @NotBlank
        String departureCity,
        @NotBlank
        String arrivalCity,
        @NotNull
        LocalDateTime departureDate,
        @NotNull
        LocalDateTime arrivalDate,
        @NotBlank
        String airline
) {
}
