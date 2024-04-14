package FlyAway.exceptions;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException() {
        super("User with given id does not exist");
    }

    public UserDoesNotExistException(String message) {
        super(message);
    }
}
