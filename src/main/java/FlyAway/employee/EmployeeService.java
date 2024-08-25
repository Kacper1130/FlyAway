package FlyAway.employee;

import FlyAway.employee.dto.AddEmployeeDto;
import FlyAway.employee.dto.EmployeeCredentialsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAll(){
        return employeeRepository.findAll();
    }

    public EmployeeCredentialsDto addEmployee(Employee employee) {
        employeeRepository.save(employee);
        return new EmployeeCredentialsDto(employee.getEmail(), employee.getPassword());
    }
}
