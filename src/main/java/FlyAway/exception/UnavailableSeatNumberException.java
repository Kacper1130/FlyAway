package FlyAway.exception;

import jakarta.validation.constraints.NotNull;

public class UnavailableSeatNumberException extends RuntimeException {
    public UnavailableSeatNumberException(@NotNull Integer seatNumber) {
        super("Seat number " + seatNumber + " is unavailable");
    }
}
