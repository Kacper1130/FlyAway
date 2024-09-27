package FlyAway.flight.airport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateAirportDto(
        @NotEmpty(message = "Name can not be empty")
        @NotBlank(message = "Name can not be blank")
        String name,
        @NotEmpty(message = "IATA code can not be empty")
        @NotBlank(message = "IATA code can not be blank")
        @JsonProperty("IATACode")
        String IATACode,
        @NotEmpty(message = "City can not be empty")
        @NotBlank(message = "City can not be blank")
        String city,
        @NotEmpty(message = "Country can not be empty")
        @NotBlank(message = "Country can not be blank")
        String country
) {
}
