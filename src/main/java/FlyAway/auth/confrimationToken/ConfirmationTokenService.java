package FlyAway.auth.confrimationToken;

import FlyAway.exception.InvalidConfirmationTokenException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmationTokenService {

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(16);
    private final ConfirmationTokenRepository confirmationTokenRepository;
    @Value("${security.confirmationToken.expirationInSeconds}")
    private long confirmationTokenExpiration;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationTokenService.class);

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public ConfirmationToken createConfirmationToken() {
        String token = Base64.encodeBase64URLSafeString(DEFAULT_TOKEN_GENERATOR.generateKey());
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusSeconds(confirmationTokenExpiration))
                .build();
        return confirmationToken;
    }

    public void save(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
        LOGGER.info("Saved new confirmation token {}", confirmationToken.toString());
    }

    public ConfirmationToken findByToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    LOGGER.error("Confirmation token {} does not exist", token);
                    return new InvalidConfirmationTokenException();
                });
    }
}
