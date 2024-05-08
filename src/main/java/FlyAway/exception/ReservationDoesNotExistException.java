package FlyAway.exception;

import java.util.UUID;

public class ReservationDoesNotExistException extends RuntimeException{
    public ReservationDoesNotExistException() {
        super("Reservation with given id does not exist");
    }

    public ReservationDoesNotExistException(String message) {
        super(message);
    }

    public ReservationDoesNotExistException(UUID id) {
        super("Reservation with id " + id + " does not exist");
    }
}
