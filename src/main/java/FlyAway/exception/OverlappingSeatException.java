package FlyAway.exception;

public class OverlappingSeatException extends RuntimeException {

    public OverlappingSeatException() {
        super();
    }

    public OverlappingSeatException(Integer s) {
        super(String.format("Overlapping seat range detected for seat %d", s));
    }
}
