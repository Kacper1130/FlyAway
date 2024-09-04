package FlyAway.auth;

import FlyAway.auth.confrimationToken.ConfirmationToken;
import FlyAway.auth.confrimationToken.ConfirmationTokenService;
import FlyAway.auth.dto.AuthenticationRequest;
import FlyAway.auth.dto.AuthenticationResponse;
import FlyAway.auth.dto.RegistrationRequest;
import FlyAway.client.Client;
import FlyAway.email.EmailService;
import FlyAway.exception.AccountNotActivatedException;
import FlyAway.exception.EmailExistsException;
import FlyAway.exception.ExpiredConfirmationTokenException;
import FlyAway.exception.UserDoesNotExistException;
import FlyAway.role.RoleRepository;
import FlyAway.security.JwtService;
import FlyAway.security.SecurityUser;
import FlyAway.user.User;
import FlyAway.user.UserRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

@Service
public class AuthenticationService {

    @Value("${application.baseUrl}")
    private String baseUrl;
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

        var userRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new IllegalStateException("ROLE CLIENT was not initialized"));

        var client = Client.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .dayOfBirth(request.dayOfBirth())
                .roles(Set.of(userRole))
                .enabled(false)
                .build();
        userRepository.save(client);
        sendConfirmationEmail(client);
        LOGGER.info("Created new client: {}", client.getEmail());
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
        var claims = new HashMap<String, Object>();
        var securityUser = ((SecurityUser) auth.getPrincipal());
        var user = securityUser.getUser();
        claims.put("firstname", user.getFirstname());
        var jwtToken = jwtService.generateToken(claims, securityUser);
        LOGGER.info("{} has logged in", user.getEmail());
        return new AuthenticationResponse(jwtToken);
    }

    private void sendConfirmationEmail(User user) throws MessagingException {
        var token = generateConfirmationToken(user);
        String verificationLink = "http://localhost:8080/api/v1/auth/confirm-account?token=" + token.getToken();
        emailService.sendConfirmationEmail(user.getEmail(), user.getFirstname(), verificationLink);
        LOGGER.info("Confirmation email has been sent to {}", user.getEmail());
    }

    private ConfirmationToken generateConfirmationToken(User user) {
        ConfirmationToken confirmationToken = confirmationTokenService.createConfirmationToken();
        confirmationToken.setUser(user);
        confirmationTokenService.save(confirmationToken);
        return confirmationToken;
    }

    public void verifyUser(String token) throws MessagingException {
        ConfirmationToken savedToken = confirmationTokenService.findByToken(token);

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            LOGGER.error("Confirmation token for {} has expired", savedToken.getUser().getEmail());
            sendConfirmationEmail(savedToken.getUser());
            throw new ExpiredConfirmationTokenException();
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> {
                    LOGGER.error("User with id {} does not exist", savedToken.getUser().getId());
                    throw new UserDoesNotExistException();
                });
        user.setEnabled(true);
        userRepository.save(user);
    }

}

