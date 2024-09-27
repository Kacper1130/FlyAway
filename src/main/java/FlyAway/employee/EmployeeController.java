package FlyAway.employee;

import FlyAway.admin.AdminController;
import FlyAway.admin.AdminService;
import FlyAway.employee.dto.AddEmployeeDto;
import FlyAway.employee.dto.EmployeeCredentialsDto;
import FlyAway.flight.dto.FlightDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@Tag(name = "Employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateEmployee() {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        LOGGER.debug("Retrieving all employees");
        List<Employee> employees = employeeService.getAll();
        LOGGER.info("Retrieved {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeCredentialsDto> addEmployee(@RequestBody @Valid AddEmployeeDto addEmployeeDto) {
        LOGGER.info("creating new employee {}", addEmployeeDto);
        EmployeeCredentialsDto createdEmployee = employeeService.createEmployee(addEmployeeDto);
        LOGGER.info("created new employee successfully");
        return ResponseEntity.ok(createdEmployee);
    }

}

