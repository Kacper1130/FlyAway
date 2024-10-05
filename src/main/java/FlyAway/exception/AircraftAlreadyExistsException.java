package FlyAway.exception;

public class AircraftAlreadyExistsException extends RuntimeException {

    public AircraftAlreadyExistsException() {
        super("Aircraft with this registration already exists");
    }
    public AircraftAlreadyExistsException(String registration) {
        super(String.format("Aircraft with registration '%s' already exists", registration));
    }
}
