package FlyAway.exception;

public class RoleNotInitializedException extends RuntimeException {

    public RoleNotInitializedException(String role) {
        super(String.format("Role %s is not initialized", role));
    }
}
