package FlyAway.exception;

public class IncorrectOldPasswordException extends RuntimeException {
    public IncorrectOldPasswordException() {
        super("The current password provided is incorrect");
    }
}
