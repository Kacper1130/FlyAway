package FlyAway.exception;

public class PasswordsDoNotMatchException extends RuntimeException {
    public PasswordsDoNotMatchException() {
        super("New password and confirmation password do not match");
    }
}
