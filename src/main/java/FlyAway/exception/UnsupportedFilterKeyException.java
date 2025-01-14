package FlyAway.exception;

public class UnsupportedFilterKeyException extends RuntimeException {
    public UnsupportedFilterKeyException(String key) {
        super(String.format("Unsupported key Exception: %s", key));
    }
}
