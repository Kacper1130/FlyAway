package FlyAway.exception;

public class PasswordsDoNotMatchException extends RuntimeException {
    public PasswordsDoNotMatchException() {
        super("New passwords are not the same");
    }
}
