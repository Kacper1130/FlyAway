package FlyAway.exception;

public class ExpiredConfirmationTokenException extends RuntimeException {

    public ExpiredConfirmationTokenException() {
        super("Token has expired. A new token has been sent to your email");
    }

}
