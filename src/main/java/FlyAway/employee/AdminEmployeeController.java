package FlyAway.employee;

import FlyAway.employee.dto.AddEmployeeDto;
import FlyAway.employee.dto.DisplayEmployeeDto;
import FlyAway.employee.dto.EmployeeCredentialsDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminEmployeeController {

    private final EmployeeService employeeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminEmployeeController.class);

    public AdminEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<DisplayEmployeeDto>> getAllEmployees() {
        LOGGER.debug("Retrieving all employees");
        List<DisplayEmployeeDto> employees = employeeService.getAll();
        LOGGER.info("Retrieved {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    @PostMapping("")
    public ResponseEntity<EmployeeCredentialsDto> createEmployee(@RequestBody @Valid AddEmployeeDto addEmployeeDto) {
        LOGGER.info("creating new employee {}", addEmployeeDto);
        EmployeeCredentialsDto createdEmployee = employeeService.createEmployee(addEmployeeDto);
        LOGGER.info("created new employee successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

}

