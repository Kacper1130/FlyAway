package FlyAway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            error.getDefaultMessage().replace(",.", ", ");
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
            errors.add(error.getDefaultMessage());
        });
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(FlightDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handleFlightDoesNotExistException(FlightDoesNotExistException exception) {
        LOGGER.error("Flight not found", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handleUserDoesNotExistException(UserDoesNotExistException exception) {
        LOGGER.error("User not found", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(ReservationDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handleReservationDoesNotExistException(ReservationDoesNotExistException exception) {
        LOGGER.error("Reservation not found", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(UserDoesNotMatchReservationUserException.class)
    public ResponseEntity<ErrorMessage> handleUserDoesNotMatchReservationUserException(UserDoesNotMatchReservationUserException exception) {
        LOGGER.error("User does not match reservation user", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<ErrorMessage> handleEmailExistsException(EmailExistsException exception) {
        LOGGER.error("Email already exists", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(IncorrectOldPasswordException.class)
    public ResponseEntity<ErrorMessage> handleIncorrectOldPasswordException(IncorrectOldPasswordException exception) {
        LOGGER.error("Old password does not match with actual password", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ResponseEntity<ErrorMessage> handlePasswordsDoNotMatchException(PasswordsDoNotMatchException exception) {
        LOGGER.error("Given passwords do not match", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthenticationException(AuthenticationException exception) {
        LOGGER.error("Authentication failed", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message("Wrong email or password")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(InvalidConfirmationTokenException.class)
    public ResponseEntity<ErrorMessage> handleInvalidConfirmationTokenException(InvalidConfirmationTokenException exception) {
        LOGGER.error("Invalid token ", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(ExpiredConfirmationTokenException.class)
    public ResponseEntity<ErrorMessage> handleExpiredConfirmationTokenException(ExpiredConfirmationTokenException exception) {
        LOGGER.error("Expired confirmation token ", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }

    @ExceptionHandler(AccountNotActivatedException.class)
    public ResponseEntity<ErrorMessage> handleAccountNotActivatedException(AccountNotActivatedException exception) {
        LOGGER.error("Account email is not verified ", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }

}
