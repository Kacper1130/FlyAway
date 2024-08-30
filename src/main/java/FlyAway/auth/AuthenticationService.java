package FlyAway.auth;

import FlyAway.auth.dto.AuthenticationRequest;
import FlyAway.auth.dto.AuthenticationResponse;
import FlyAway.auth.dto.RegistrationRequest;
import FlyAway.client.Client;
import FlyAway.exception.EmailExistsException;
import FlyAway.role.RoleRepository;
import FlyAway.security.JwtService;
import FlyAway.security.SecurityUser;
import FlyAway.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;

@Service
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailExistsException(request.email());
        }

        var userRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new IllegalStateException("ROLE CLIENT was not initialized"));

        var user = Client.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .dayOfBirth(request.dayOfBirth())
                .roles(Set.of(userRole))
                .enabled(true)
                .build();
        userRepository.save(user);
        LOGGER.info("Created new client: {}", user.getEmail());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //todo sprawdzenie czy istnieje taki uzytkownik przed authenticate,
        // nie ma errorow w przypadku braku authentykacji tylko 403
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
        return new AuthenticationResponse(jwtToken);
    }

}

