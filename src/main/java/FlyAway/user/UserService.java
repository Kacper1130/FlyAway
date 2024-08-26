package FlyAway.user;

import FlyAway.exception.IncorrectOldPasswordException;
import FlyAway.exception.PasswordsDoNotMatchException;
import FlyAway.security.SecurityUser;
import FlyAway.user.dto.ChangePasswordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest, Authentication authentication) {
        var user = ((SecurityUser) authentication.getPrincipal()).getUser();

        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
            LOGGER.error("Old password does not match with user password");
            throw new IncorrectOldPasswordException();
        }

        if (!changePasswordRequest.newPassword1().equals(changePasswordRequest.newPassword2())) {
            LOGGER.error("Passwords are not the same");
            throw new PasswordsDoNotMatchException();
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword1()));
        userRepository.save(user);
        LOGGER.info("{} has changed password successfully", user.getEmail());
    }
}
