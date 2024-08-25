package FlyAway.admin;

import FlyAway.client.ClientService;
import FlyAway.employee.Employee;
import FlyAway.employee.EmployeeService;
import FlyAway.employee.dto.AddEmployeeDto;
import FlyAway.employee.dto.EmployeeCredentialsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final EmployeeService employeeService;
    private final AdminService adminService;

    public AdminController(EmployeeService employeeService, AdminService adminService) {
        this.employeeService = employeeService;
        this.adminService = adminService;
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @PostMapping("/employees/add")
    public ResponseEntity<?> addEmployee(AddEmployeeDto addEmployeeDto) {
        EmployeeCredentialsDto createdEmployee = adminService.createEmployee(addEmployeeDto);
        return ResponseEntity.ok(createdEmployee);
    }
}
