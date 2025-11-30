package FlyAway.auth.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AuthenticationResponse(@NotNull UUID userId, @NotNull String role, @NotNull String firstname, @NotNull String email) {
}
