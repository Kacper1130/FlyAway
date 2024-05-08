package FlyAway.exception;

public class EmailExistsException extends RuntimeException{
    public EmailExistsException(String email) {
        super("There is already an account with email: " + email);
    }
}
