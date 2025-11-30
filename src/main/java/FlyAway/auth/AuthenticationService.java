package FlyAway.auth;

import FlyAway.auth.dto.AuthenticationRequest;
import FlyAway.auth.dto.AuthenticationResponse;
import FlyAway.auth.dto.ChangePasswordRequest;
import FlyAway.auth.dto.RegistrationRequest;
import FlyAway.exception.EmailExistsException;
import FlyAway.exception.IncorrectOldPasswordException;
import FlyAway.exception.PasswordsDoNotMatchException;
import FlyAway.role.Role;
import FlyAway.security.SecurityUser;
import FlyAway.user.User;
import FlyAway.user.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public void register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailExistsException(request.email());
        }

        var client = createClientFromRegistrationRequest(request);

        userRepository.save(client);
        LOGGER.info("Created new client: {}", client.getEmail());
    }

    private User createClientFromRegistrationRequest(RegistrationRequest request) {

        return User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(request.password())
                .phoneNumber(request.phoneNumber())
                .dayOfBirth(request.dayOfBirth())
                .role(Role.ROLE_CLIENT)
                .enabled(true)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var securityUser = ((SecurityUser) auth.getPrincipal());
        var user = securityUser.getUser();

        if (user.getRole().name().equals("ROLE_EMPLOYEE")) {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }

        LOGGER.info("{} has logged in", user.getEmail());

        String roleName = user.getRole().name();

        return new AuthenticationResponse(user.getId(), roleName, user.getFirstname(), user.getEmail());
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest, Authentication authentication) {
        var user = ((SecurityUser) authentication.getPrincipal()).getUser();

        if (!changePasswordRequest.newPassword().equals(changePasswordRequest.confirmPassword())) {
            LOGGER.error("New password and confirmation do not match for user: {}", user.getEmail());
            throw new PasswordsDoNotMatchException();
        }

        if (!changePasswordRequest.newPassword().equals(user.getPassword())) {
            LOGGER.error("Incorrect current password provided for user: {}", user.getEmail());
            throw new IncorrectOldPasswordException();
        }

        user.setPassword(changePasswordRequest.newPassword());
        userRepository.save(user);
        LOGGER.info("{} has changed password successfully", user.getEmail());
    }

}

