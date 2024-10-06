package FlyAway.employee.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DisplayEmployeeDto(
        String firstname,
        String lastname,
        String email,
        String phoneNumber,
        LocalDate hireDate,
        LocalDateTime lastLogin
) {
}
