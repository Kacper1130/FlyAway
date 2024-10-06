package FlyAway.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DisplayEmployeeDto(
        String firstname,
        String lastname,
        String email,
        String phoneNumber,
        LocalDate hireDate,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime lastLogin
) {
}
