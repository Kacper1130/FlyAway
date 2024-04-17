package FlyAway.user;

import FlyAway.exceptions.EmailExistsException;
import FlyAway.exceptions.ReservationDoesNotExistException;
import FlyAway.exceptions.UserDoesNotExistException;
import FlyAway.reservation.Reservation;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.user.dto.UserDto;
import FlyAway.user.dto.UserRegistrationDto;
import FlyAway.user.dto.UserReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
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
        LOGGER.debug("Retrieving all Users");
        List<UserDto> users = userService.getAll();
        LOGGER.info("Retrieved {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody UserRegistrationDto userRegistrationDto) {
        LOGGER.debug("Adding new user " + userRegistrationDto);
        try{
            UserDto userDto = userService.addUser(userRegistrationDto);
            LOGGER.info("Added new user " + userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (EmailExistsException e) {
            LOGGER.error("User with email {} already exists",userRegistrationDto.email());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with given email already exits", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReservationDto> getUserWithReservations(@PathVariable Long id) {
        LOGGER.debug("Retrieving user with id " + id);
        try {
            UserReservationDto userReservationDto = userService.getUserWithReservations(id);
            LOGGER.info("Successfully retrieved user with reservations");
            return ResponseEntity.ok(userReservationDto);
        } catch (UserDoesNotExistException e) {
            LOGGER.error("User with id {} does not exist", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given id not found");
        }

    }

    @PutMapping("/{userId}/{reservationId}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long userId, @PathVariable UUID reservationId) {
        LOGGER.debug("Cancelling reservation, user id {}, reservation id {}", userId,reservationId);
        try{
            userService.cancelReservation(userId,reservationId);
            LOGGER.info("Successfully cancelled reservation");
            return ResponseEntity.ok("Cancelled reservation");
        } catch (UserDoesNotExistException e ) {
            LOGGER.error("User with id {} does not exist", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given id not found");
        } catch (ReservationDoesNotExistException e ) {
            LOGGER.error("Reservation with id {} does not exist",reservationId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation with given id not found");
        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
