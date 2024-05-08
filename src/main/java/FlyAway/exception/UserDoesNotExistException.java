package FlyAway.exception;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException() {
        super("User with given id does not exist");
    }

    public UserDoesNotExistException(String message) {
        super(message);
    }

    public UserDoesNotExistException(Long id) {
        super("User with id " + id + " does not exist");
    }
}
