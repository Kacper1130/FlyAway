package FlyAway.user;

import FlyAway.user.dto.UserRegistrationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody UserRegistrationDto userRegistrationDto) {
        User user = userService.addUser(userRegistrationDto);
        return ResponseEntity.created(URI.create("/api/v1/users/add" + user.getId())).body(user);
    }
}