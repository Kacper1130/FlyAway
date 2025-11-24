package FlyAway.security;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public String generatePassword() {
        return "password12345";
    }
}
