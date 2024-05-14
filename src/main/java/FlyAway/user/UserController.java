package FlyAway.user;

import FlyAway.reservation.dto.ReservationDto;
import FlyAway.user.dto.UserDto;
import FlyAway.user.dto.UserRegistrationDto;
import FlyAway.user.dto.UserReservationDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        LOGGER.debug("Retrieving all active users");
        List<UserDto> users = userService.getAllActiveUsers();
        LOGGER.info("Retrieved {} active users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<UserReservationDto>> getAllDeletedUsers() {
        LOGGER.debug("Retrieving deleted Users");
        List<UserReservationDto> users = userService.getAllDeletedUsers();
        LOGGER.info("Retrieved {} deleted users", users.size());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        LOGGER.debug("Adding new user " + userRegistrationDto);
        UserDto userDto = userService.addUser(userRegistrationDto);
        LOGGER.info("Added new user " + userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        LOGGER.debug("Retrieving user with id {}", id);
        UserDto userDto = userService.getUser(id);
        LOGGER.info("Successfully retrieved user");
        return ResponseEntity.ok(userDto);

    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<UserReservationDto> getUserWithReservations(@PathVariable Long id) {
        LOGGER.debug("Retrieving user's reservation, user id " + id);
        UserReservationDto userReservationDto = userService.getUserWithReservations(id);
        LOGGER.info("Successfully retrieved user with reservations");
        return ResponseEntity.ok(userReservationDto);

    }

    @GetMapping("/{id}/reservations/{reservationId}")
    public ResponseEntity<ReservationDto> getUserReservation(@PathVariable("id") Long userId, @PathVariable UUID reservationId) {
        LOGGER.debug("Retrieving user reservation, user id {}, reservation id {}", userId, reservationId);
        ReservationDto reservationDto = userService.getUserReservation(userId, reservationId);
        return ResponseEntity.ok(reservationDto);

    }

    @DeleteMapping("/{userId}/reservations/{reservationId}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long userId, @PathVariable UUID reservationId) {
        LOGGER.debug("Cancelling reservation, user id {}, reservation id {}", userId, reservationId);
        userService.cancelReservation(userId, reservationId);
        LOGGER.info("Successfully cancelled reservation");
        return ResponseEntity.ok("Cancelled reservation");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        LOGGER.debug("Deleting user with id {}", id);
        userService.deleteUser(id);
        LOGGER.info("Successfully deleted user with id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
