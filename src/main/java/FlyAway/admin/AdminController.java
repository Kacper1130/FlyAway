package FlyAway.admin;

import FlyAway.auth.AuthenticationController;
import FlyAway.client.ClientService;
import FlyAway.employee.Employee;
import FlyAway.employee.EmployeeService;
import FlyAway.employee.dto.AddEmployeeDto;
import FlyAway.employee.dto.EmployeeCredentialsDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "admin")
public class AdminController {

    private final EmployeeService employeeService;
    private final AdminService adminService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);


    public AdminController(EmployeeService employeeService, AdminService adminService) {
        this.employeeService = employeeService;
        this.adminService = adminService;
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @PostMapping("/employees/add")
    public ResponseEntity<EmployeeCredentialsDto> addEmployee(@RequestBody @Valid AddEmployeeDto addEmployeeDto) {
        LOGGER.info("creating new employee {}", addEmployeeDto);
        EmployeeCredentialsDto createdEmployee = adminService.createEmployee(addEmployeeDto);
        LOGGER.info("created new employee successfully");
        return ResponseEntity.ok(createdEmployee);
    }
}
