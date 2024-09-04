package FlyAway.exception;

public class InvalidConfirmationTokenException extends RuntimeException {

    public InvalidConfirmationTokenException() {
        super("Invalid token");
    }
}
