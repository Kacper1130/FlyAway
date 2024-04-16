package FlyAway.user;

import FlyAway.user.dto.UserDto;
import FlyAway.user.dto.UserRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<?> add(@RequestBody UserRegistrationDto userRegistrationDto) { //TODO add catch try
        LOGGER.debug("Adding new user " + userRegistrationDto);
        UserDto user = userService.addUser(userRegistrationDto);
        LOGGER.info("Added new user " + user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
