package FlyAway.exceptions;

public class UserDoesNotMatchReservationUserException extends RuntimeException {
    public UserDoesNotMatchReservationUserException() {
        super("User does not match with reservation user");
    }

    public UserDoesNotMatchReservationUserException(String message) {
        super(message);
    }
}
