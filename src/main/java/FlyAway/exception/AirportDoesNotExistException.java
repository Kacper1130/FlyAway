package FlyAway.exception;

public class AirportDoesNotExistException extends RuntimeException {

    public AirportDoesNotExistException() {
        super("Airport with given id does not exist");
    }
}
