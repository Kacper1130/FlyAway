package FlyAway.flight.aircraft.dto;

public record AircraftDto(
        String model,
        Integer productionYear,
        String registration,
        Integer totalSeats
) {
}
