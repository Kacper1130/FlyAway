package FlyAway.employee.dto;

public record EmployeeCredentialsDto(
        String firstname,
        String lastname,
        String email,
        String password
) {
}
