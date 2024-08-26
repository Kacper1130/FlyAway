package FlyAway.user.dto;

import FlyAway.validation.Password;

public record ChangePasswordRequest(
        String oldPassword,
        @Password String newPassword1,
        @Password String newPassword2
) {
}
