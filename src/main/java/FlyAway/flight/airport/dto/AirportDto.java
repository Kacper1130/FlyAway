package FlyAway.flight.airport.dto;

import FlyAway.flight.country.dto.CountryDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AirportDto(
        @NotNull(message = "Id can not be null")
        UUID id,
        @NotEmpty(message = "Name can not be empty")
        @NotBlank(message = "Name can not be blank")
        String name,
        @NotEmpty(message = "IATA code can not be empty")
        @NotBlank(message = "IATA code can not be blank")
        String IATACode,
        @NotEmpty(message = "City can not be empty")
        @NotBlank(message = "City can not be blank")
        String city,
        boolean enabled,
        @NotNull(message = "Country can not be null")
        @Valid
        CountryDto country
) {
}
