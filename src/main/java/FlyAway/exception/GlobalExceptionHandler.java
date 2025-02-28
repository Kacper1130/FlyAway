package FlyAway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException exception) {
        LOGGER.error("Access denied exception", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
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

    @ExceptionHandler(CountryDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handleCountryDoesNotExistException(CountryDoesNotExistException exception) {
        LOGGER.error("Country not found", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(InvalidSeatRangeException.class)
    public ResponseEntity<ErrorMessage> handleInvalidSeatRangeException(InvalidSeatRangeException exception) {
        LOGGER.error("Invalid seat range", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(TotalSeatsMismatchException.class)
    public ResponseEntity<ErrorMessage> handleTotalSeatsMismatchException(TotalSeatsMismatchException exception) {
        LOGGER.error("Total seats mismatch", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(OverlappingSeatException.class)
    public ResponseEntity<ErrorMessage> handleOverlappingSeatException(OverlappingSeatException exception) {
        LOGGER.error("Overlapping seat range", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(AircraftAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleAircraftAlreadyExistsException(AircraftAlreadyExistsException exception) {
        LOGGER.error("Aircraft already exists", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(AirportDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handleAirportDoesNotExistException(AirportDoesNotExistException exception) {
        LOGGER.error("Airport not found", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(MissingCabinClassPriceException.class)
    public ResponseEntity<ErrorMessage> handleMissingCabinClassPriceException(MissingCabinClassPriceException exception) {
        LOGGER.error("Missing price for cabin class", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(UnsupportedFilterKeyException.class)
    public ResponseEntity<ErrorMessage> handleUnsupportedFilterKeyException(UnsupportedFilterKeyException exception) {
        LOGGER.error("Unsupported key exception", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(CabinClassDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handleCabinClassDoesNotExistException(CabinClassDoesNotExistException exception) {
        LOGGER.error("Cabin class does not exist", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(SeatNumberDoesNotBelongToAnyCabinClassException.class)
    public ResponseEntity<ErrorMessage> handleSeatNumberDoesNotBelongToAnyCabinClassException(SeatNumberDoesNotBelongToAnyCabinClassException exception) {
        LOGGER.error("Seat number does not belong to any cabin class", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(UnavailableSeatNumberException.class)
    public ResponseEntity<ErrorMessage> handleUnavailableSeatNumberException(UnavailableSeatNumberException exception) {
        LOGGER.error("Seat number is unavailable", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(SupportTicketDoesNotExistException.class)
    public ResponseEntity<ErrorMessage> handleSupportTicketDoesNotExistException(SupportTicketDoesNotExistException exception) {
        LOGGER.error("Support ticket does not exist", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(TicketOperationException.class)
    public ResponseEntity<ErrorMessage> handleTicketOperationException(TicketOperationException exception) {
        LOGGER.error("Can not perform operation exception", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(RoleNotInitializedException.class)
    public ResponseEntity<ErrorMessage> handleRoleNotInitializedException(RoleNotInitializedException exception) {
        LOGGER.error("Role not initialized", exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

}
