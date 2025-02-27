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
import FlyAway.role.Role;
import FlyAway.role.RoleRepository;
import FlyAway.security.JwtService;
import FlyAway.security.SecurityUser;
import FlyAway.user.User;
import FlyAway.user.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private static final RegistrationRequest REGISTRATION_REQUEST = new RegistrationRequest(
            "John",
            "Smith",
            "example@email.com",
            "password123",
            "1234567890",
            LocalDate.of(1990, 10, 10)
    );

    private static final AuthenticationRequest AUTHENTICATION_REQUEST = new AuthenticationRequest(
            "example@email.com",
            "password123"
    );

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private JwtService jwtService;

    @Mock
    private SecurityUser securityUser;
    @Mock
    private Employee employee;
    @Mock
    private Client client;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    void register_WhenEmailAlreadyExists_ShouldThrowEmailExistsException() {
        when(userRepository.existsByEmail(REGISTRATION_REQUEST.email())).thenReturn(true);

        assertThrows(EmailExistsException.class, () -> authenticationService.register(REGISTRATION_REQUEST));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_WhenValidRequest_ShouldSaveNewClient() throws MessagingException {
        Role clientRole = new Role();
        clientRole.setName("ROLE_CLIENT");
        when(userRepository.existsByEmail(REGISTRATION_REQUEST.email())).thenReturn(false);
        when(roleRepository.findByName("ROLE_CLIENT")).thenReturn(Optional.of(clientRole));
        when(passwordEncoder.encode(REGISTRATION_REQUEST.password())).thenReturn("encodedPassword");
        ConfirmationToken token = ConfirmationToken.builder()
                .token("test-token")
                .expiresAt(LocalDateTime.now().plusSeconds(3600))
                .build();
        when(confirmationTokenService.createConfirmationToken()).thenReturn(token);

        authenticationService.register(REGISTRATION_REQUEST);

        verify(userRepository, times(1)).save(any(Client.class));
        verify(emailService, times(1)).sendConfirmationEmail(eq(REGISTRATION_REQUEST.email()), eq(REGISTRATION_REQUEST.firstname()), anyString());
    }

    @Test
    void authenticate_WhenAccountNotActivated_ShouldThrowAccountNotActivatedException() {
        when(userRepository.existsByEmailAndEnabledFalse(AUTHENTICATION_REQUEST.email())).thenReturn(true);

        assertThrows(AccountNotActivatedException.class, () -> authenticationService.authenticate(AUTHENTICATION_REQUEST));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void authenticate_WhenValidCredentialsForEmployee_ShouldReturnAuthenticationResponse() {
        when(userRepository.existsByEmailAndEnabledFalse(AUTHENTICATION_REQUEST.email())).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(securityUser.getUser()).thenReturn(employee);
        when(jwtService.generateToken(anyMap(), eq(securityUser))).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.authenticate(AUTHENTICATION_REQUEST);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        verify(employee, times(1)).setLastLogin(any(LocalDateTime.class));
        verify(userRepository, times(1)).save(employee);
        verify(jwtService, times(1)).generateToken(anyMap(), eq(securityUser));
    }

    @Test
    void authenticate_WhenValidCredentialsForNonEmployee_ShouldReturnAuthenticationResponse() {
        when(userRepository.existsByEmailAndEnabledFalse(AUTHENTICATION_REQUEST.email())).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(securityUser.getUser()).thenReturn(client);
        when(jwtService.generateToken(anyMap(), eq(securityUser))).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.authenticate(AUTHENTICATION_REQUEST);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        verify(employee, times(0)).setLastLogin(any(LocalDateTime.class));
        verify(userRepository, times(0)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(anyMap(), eq(securityUser));
    }

    @Test
    void authenticate_WhenInvalidCredentials_ShouldThrowAuthenticationException() {
        when(userRepository.existsByEmailAndEnabledFalse(AUTHENTICATION_REQUEST.email())).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Authentication exception"));

        assertThrows(AuthenticationException.class, () -> authenticationService.authenticate(AUTHENTICATION_REQUEST));
        verify(jwtService, never()).generateToken(any(), any());
    }

    @Test
    void verifyUser_WhenTokenValid_ShouldEnableUser() throws MessagingException {
        Client realClient = new Client();
        realClient.setId(1L);
        realClient.setEmail("client@example.com");
        realClient.setEnabled(false);

        String validToken = "valid-token";

        ConfirmationToken token = new ConfirmationToken();
        token.setToken(validToken);
        token.setUser(realClient);
        token.setExpiresAt(LocalDateTime.now().plusHours(24));

        when(userRepository.findById(token.getUser().getId())).thenReturn(Optional.of(realClient));
        when(confirmationTokenService.findByToken(validToken)).thenReturn(token);

        authenticationService.verifyUser(validToken);

        assertTrue(realClient.isEnabled());
        verify(userRepository, times(1)).save(realClient);
    }

    @Test
    void verifyUser_WhenTokenExpired_ShouldThrowExpiredConfirmationTokenException() throws MessagingException {
        Client realClient = new Client();
        realClient.setId(1L);
        realClient.setFirstname("client-firstname");
        realClient.setEmail("client@example.com");
        realClient.setEnabled(false);

        String expiredToken = "expired-token";

        ConfirmationToken token = new ConfirmationToken();
        token.setToken(expiredToken);
        token.setUser(realClient);
        token.setExpiresAt(LocalDateTime.now().minusHours(24));

        when(userRepository.findById(token.getUser().getId())).thenReturn(Optional.of(realClient));
        when(confirmationTokenService.findByToken(expiredToken)).thenReturn(token);
        ConfirmationToken newToken = ConfirmationToken.builder()
                .token("test-token")
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
        when(confirmationTokenService.createConfirmationToken()).thenReturn(newToken);

        assertThrows(ExpiredConfirmationTokenException.class, () ->
                authenticationService.verifyUser(expiredToken)
        );
        verify(emailService, times(1)).sendConfirmationEmail(eq(realClient.getEmail()), eq(realClient.getFirstname()), anyString());
        verify(userRepository, never()).save(realClient);
    }

    @Test
    void verifyUser_WhenUserDoesNotExist_ShouldThrowUserDoesNotExistException() {
        Client tokenUser = new Client();
        tokenUser.setId(1L);
        tokenUser.setEmail("client@example.com");

        String validToken = "valid-token";

        ConfirmationToken token = new ConfirmationToken();
        token.setToken(validToken);
        token.setUser(tokenUser);
        token.setExpiresAt(LocalDateTime.now().plusHours(24));

        when(confirmationTokenService.findByToken(validToken)).thenReturn(token);
        when(userRepository.findById(tokenUser.getId())).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class, () ->
                authenticationService.verifyUser(validToken)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void verifyUser_WhenTokenNotFound_ShouldThrowInvalidConfirmationTokenException() {
        String nonExistentToken = "non-existent-token";
        when(confirmationTokenService.findByToken(nonExistentToken))
                .thenThrow(new InvalidConfirmationTokenException());

        assertThrows(InvalidConfirmationTokenException.class, () ->
                authenticationService.verifyUser(nonExistentToken)
        );

        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_WhenCurrentPasswordInvalid_ShouldThrowIncorrectOldPasswordException() {
        Client realClient = new Client();
        realClient.setPassword("currentPassword");

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("wrongPassword", "newPassword", "newPassword");
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(securityUser.getUser()).thenReturn(realClient);
        when(passwordEncoder.matches("wrongPassword", realClient.getPassword())).thenReturn(false);

        assertThrows(IncorrectOldPasswordException.class, () -> authenticationService.changePassword(changePasswordRequest, authentication));
        verify(userRepository, never()).save(any());
    }


    @Test
    void changePassword_WhenNewPasswordsDoNotMatch_ShouldThrowPasswordsDoNotMatchException() {
        Client realClient = new Client();
        realClient.setPassword("currentPassword");
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("currentPassword", "newPassword", "differentPassword");

        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(securityUser.getUser()).thenReturn(realClient);
        when(passwordEncoder.matches("currentPassword", realClient.getPassword())).thenReturn(true);

        assertThrows(PasswordsDoNotMatchException.class, () -> authenticationService.changePassword(changePasswordRequest, authentication));
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_WhenRequestValid_ShouldUpdatePassword() {
        Client realClient = new Client();
        realClient.setPassword("currentPassword");
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("currentPassword", "newPassword", "newPassword");

        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(securityUser.getUser()).thenReturn(realClient);
        when(passwordEncoder.matches("currentPassword", realClient.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        authenticationService.changePassword(changePasswordRequest, authentication);

        verify(userRepository).save(realClient);
        assertEquals("encodedNewPassword", realClient.getPassword());
    }



}