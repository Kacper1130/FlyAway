package FlyAway.auth;

import FlyAway.auth.confrimationToken.ConfirmationToken;
import FlyAway.auth.confrimationToken.ConfirmationTokenService;
import FlyAway.auth.dto.AuthenticationRequest;
import FlyAway.auth.dto.AuthenticationResponse;
import FlyAway.auth.dto.ChangePasswordRequest;
import FlyAway.auth.dto.RegistrationRequest;
import FlyAway.client.Client;
import FlyAway.email.EmailService;
import FlyAway.employee.Employee;
import FlyAway.exception.*;
import FlyAway.role.RoleRepository;
import FlyAway.security.JwtService;
import FlyAway.security.SecurityUser;
import FlyAway.user.User;
import FlyAway.user.UserRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

@Service
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final JwtService jwtService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, ConfirmationTokenService confirmationTokenService, EmailService emailService, JwtService jwtService) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    public void register(RegistrationRequest request) throws MessagingException {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailExistsException(request.email());
        }

        var client = createClientFromRegistrationRequest(request);

        userRepository.save(client);
        sendConfirmationEmail(client);
        LOGGER.info("Created new client: {}", client.getEmail());
    }

    private Client createClientFromRegistrationRequest(RegistrationRequest request) {
        var userRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new IllegalStateException("ROLE CLIENT was not initialized"));

        return Client.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .dayOfBirth(request.dayOfBirth())
                .roles(Set.of(userRole))
                .enabled(false)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if (userRepository.existsByEmailAndEnabledFalse(request.email())) {
            throw new AccountNotActivatedException();
        }
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var securityUser = ((SecurityUser) auth.getPrincipal());
        var user = securityUser.getUser();

        if (user instanceof Employee employee) {
            employee.setLastLogin(LocalDateTime.now());
            userRepository.save(employee);
        }

        var claims = new HashMap<String, Object>();
        claims.put("firstname", user.getFirstname());
        var jwtToken = jwtService.generateToken(claims, securityUser);
        LOGGER.info("{} has logged in", user.getEmail());
        return new AuthenticationResponse(jwtToken);
    }

    private void sendConfirmationEmail(User user) throws MessagingException {
        var token = generateConfirmationToken(user);
        String verificationLink = "http://localhost:4200/activate-account?token=" + token.getToken();
        emailService.sendConfirmationEmail(user.getEmail(), user.getFirstname(), verificationLink);
        LOGGER.info("Confirmation email has been sent to {}", user.getEmail());
    }

    private ConfirmationToken generateConfirmationToken(User user) {
        LOGGER.info("generating confirmation token for user {}", user.getEmail());
        ConfirmationToken confirmationToken = confirmationTokenService.createConfirmationToken();
        confirmationToken.setUser(user);
        confirmationTokenService.save(confirmationToken);
        LOGGER.info("saved new token {}", confirmationToken.getToken());
        return confirmationToken;
    }

    public void verifyUser(String token) throws MessagingException {
        ConfirmationToken savedToken = confirmationTokenService.findByToken(token);

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> {
                    LOGGER.error("User with id {} does not exist", savedToken.getUser().getId());
                    return new UserDoesNotExistException();
                });

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            LOGGER.error("Confirmation token for {} has expired", savedToken.getUser().getEmail());
            sendConfirmationEmail(savedToken.getUser());
            throw new ExpiredConfirmationTokenException();
        }

        user.setEnabled(true);
        userRepository.save(user);
        LOGGER.info("{} verified successfully", user.getEmail());
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest, Authentication authentication) {
        var user = ((SecurityUser) authentication.getPrincipal()).getUser();

        if (!passwordEncoder.matches(changePasswordRequest.currentPassword(), user.getPassword())) {
            LOGGER.error("Incorrect current password provided for user: {}", user.getEmail());
            throw new IncorrectOldPasswordException();
        }

        if (!changePasswordRequest.newPassword().equals(changePasswordRequest.confirmPassword())) {
            LOGGER.error("New password and confirmation do not match for user: {}", user.getEmail());
            throw new PasswordsDoNotMatchException();
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
        userRepository.save(user);
        LOGGER.info("{} has changed password successfully", user.getEmail());
    }

}

