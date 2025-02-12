package FlyAway.flight.airport.dto;

public record AirportBasicDto(
        String name,
        String IATACode,
        String city,
        String country
) {
}
