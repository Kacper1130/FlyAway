package FlyAway.employee.dto;

import FlyAway.validation.Password;
import FlyAway.validation.PhoneNumber;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddEmployeeDto(
        @NotBlank
        @Size(min = 2)
        String firstname,
        @NotBlank
        @Size(min = 2)
        String lastname,
        @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                message = "Email should be valid")
        String email,
        @PhoneNumber
        String phoneNumber
) {


}
