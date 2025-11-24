package FlyAway.auth.dto;

import jakarta.validation.constraints.NotNull;

public record AuthenticationResponse(@NotNull Long userId, @NotNull String role, @NotNull String firstname, @NotNull String email) {
}
