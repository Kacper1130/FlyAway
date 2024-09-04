package FlyAway.auth;

import FlyAway.auth.dto.AuthenticationRequest;
import FlyAway.auth.dto.AuthenticationResponse;
import FlyAway.auth.dto.RegistrationRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        LOGGER.debug("Registering new user: {}", request);
        authenticationService.register(request);
        LOGGER.info("Registered new user successfully");
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        LOGGER.debug("{} is trying to log in", request.email());
        var response = authenticationService.authenticate(request);
        LOGGER.info("{} successfully logged in ", request.email());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) throws MessagingException {
        authenticationService.verifyUser(confirmationToken);
        return ResponseEntity.ok().build();
    }

}


