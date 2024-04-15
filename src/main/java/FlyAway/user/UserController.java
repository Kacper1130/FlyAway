package FlyAway.user;

import FlyAway.user.dto.UserRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseEntity<List<User>> getAll() {  //TODO User -> UserDto ?
        LOGGER.debug("Retrieving all Users");
        List<User> users = userService.getAll();
        LOGGER.info("Retrieved {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody UserRegistrationDto userRegistrationDto) { //TODO add catch try
        LOGGER.debug("Adding new user " + userRegistrationDto);
        User user = userService.addUser(userRegistrationDto);
        LOGGER.info("Added user with id {} ", user.getId());
        return ResponseEntity.created(URI.create("/api/v1/users/add" + user.getId())).body(user);
    }
}
