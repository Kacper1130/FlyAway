package FlyAway.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record AuthenticationRequest(

        @NotEmpty(message = "Email can not be empty")
        @NotBlank(message = "Email can not be blank")
        @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                message = "Email should be valid")
        String email,
        @NotEmpty(message = "Password can not be empty")
        @NotBlank(message = "Password can not be blank")
        String password
) {
}
