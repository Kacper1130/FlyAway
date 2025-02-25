package FlyAway.auth.dto;

import FlyAway.validation.Password;

public record ChangePasswordRequest(
        String currentPassword,
        @Password String newPassword,
        @Password String confirmPassword
) {
}
