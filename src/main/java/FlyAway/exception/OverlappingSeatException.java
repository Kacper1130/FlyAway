package FlyAway.exception;

public class OverlappingSeatException extends RuntimeException {

    public OverlappingSeatException() {
        super("Seat ranges overlap between cabin classes");
    }

    public OverlappingSeatException(Integer s) {
        super(String.format("Overlapping seat range detected for seat %d", s));
    }
}
