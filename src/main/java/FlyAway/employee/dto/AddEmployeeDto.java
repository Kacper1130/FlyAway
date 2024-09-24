package FlyAway.employee.dto;

import FlyAway.validation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AddEmployeeDto(
        @NotEmpty(message = "Firstname can not be empty")
        @NotBlank(message = "Firstname can not be blank")
        @Size(min = 2, message = "Firstname is too short")
        String firstname,
        @NotEmpty(message = "Lastname can not be empty")
        @NotBlank(message = "Lastname can not be blank")
        @Size(min = 2, message = "Lastname is too short")
        String lastname,
        @NotEmpty(message = "Email can not be empty")
        @NotBlank(message = "Email can not be blank")
        @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                message = "Email should be valid")
        String email,
        @NotEmpty(message = "Phone number can not be empty")
        @NotBlank(message = "Phone number can not be blank")
        @PhoneNumber
        String phoneNumber
) {


}
