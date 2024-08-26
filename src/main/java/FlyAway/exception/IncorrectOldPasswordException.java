package FlyAway.exception;

public class IncorrectOldPasswordException extends RuntimeException {
    public IncorrectOldPasswordException() {
        super("Old password does not match with user password");
    }
}
