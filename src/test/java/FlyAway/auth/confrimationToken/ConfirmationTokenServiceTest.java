package FlyAway.auth.confrimationToken;

import FlyAway.exception.InvalidConfirmationTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTest {

    @InjectMocks
    ConfirmationTokenService confirmationTokenService;

    @Mock
    ConfirmationTokenRepository confirmationTokenRepository;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(confirmationTokenService, "confirmationTokenExpiration", 3600L);
    }

    @Test
    void createConfirmationToken_ShouldCreateValidToken() {
        ConfirmationToken confirmationToken = confirmationTokenService.createConfirmationToken();

        Assertions.assertNotNull(confirmationToken);
        Assertions.assertNotNull(confirmationToken.getToken());
        Assertions.assertNotNull(confirmationToken.getExpiresAt());
        assertTrue(confirmationToken.getExpiresAt().isAfter(LocalDateTime.now()));
        assertTrue(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now().plusSeconds(3601)));
    }

    @Test
    void createConfirmationToken_ShouldCreateUniqueTokens() {
        ConfirmationToken token1 = confirmationTokenService.createConfirmationToken();
        ConfirmationToken token2 = confirmationTokenService.createConfirmationToken();

        assertNotEquals(token1.getToken(), token2.getToken());
    }

    @Test
    void save_ShouldCallRepository() {
        ConfirmationToken token = ConfirmationToken.builder()
                .token("test-token")
                .expiresAt(LocalDateTime.now().plusSeconds(3600))
                .build();
        confirmationTokenService.save(token);

        verify(confirmationTokenRepository, times(1)).save(token);
    }

    @Test
    void findByToken_WhenTokenExist_ShouldReturnToken(){
        String token = "existingToken";
        ConfirmationToken expectedToken = ConfirmationToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusSeconds(3600))
                .build();

        when(confirmationTokenRepository.findByToken(token)).thenReturn(Optional.of(expectedToken));

        ConfirmationToken foundToken = confirmationTokenService.findByToken(token);

        assertNotNull(foundToken);
        assertEquals(expectedToken, foundToken);
        verify(confirmationTokenRepository, times(1)).findByToken(token);
    }

    @Test
    void findByToken_WhenTokenDoesNotExist_ShouldReturnToken() {
        String token = "fakeToken";
        when(confirmationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(InvalidConfirmationTokenException.class, () -> confirmationTokenService.findByToken(token));
        verify(confirmationTokenRepository, times(1)).findByToken(token);
    }

}