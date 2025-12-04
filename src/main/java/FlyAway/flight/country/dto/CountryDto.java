package FlyAway.flight.country.dto;

import java.util.UUID;

public record CountryDto(
        UUID id,
        String name,
        boolean enabled

) {
}
