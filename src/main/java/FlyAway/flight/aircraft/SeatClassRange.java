package FlyAway.flight.aircraft;

import jakarta.persistence.Embeddable;

@Embeddable
public record SeatClassRange(
        Integer startSeat,
        Integer endSeat
) {


}
