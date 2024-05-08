package FlyAway.exception;

public class FlightDoesNotExistException extends RuntimeException{
    public FlightDoesNotExistException() {
        super("Flight with given id does not exist");
    }

    public FlightDoesNotExistException(String message) {
        super(message);
    }
}
