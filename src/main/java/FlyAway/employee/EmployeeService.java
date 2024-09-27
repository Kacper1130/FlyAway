package FlyAway.employee;

import FlyAway.employee.dto.AddEmployeeDto;
import FlyAway.employee.dto.EmployeeCredentialsDto;
import FlyAway.exception.EmailExistsException;
import FlyAway.role.RoleRepository;
import FlyAway.security.PasswordService;
import FlyAway.user.UserRepository;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;

    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordService passwordService) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
    }

    public List<Employee> getAll(){
        return employeeRepository.findAll();
    }

    public EmployeeCredentialsDto createEmployee(AddEmployeeDto addEmployeeDto) {
        if (userRepository.existsByEmail(addEmployeeDto.email())) {
            throw new EmailExistsException(addEmployeeDto.email());
        }

        var employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new IllegalStateException("ROLE EMPLOYEE was not initialized"));

        String generatedPassword = passwordService.generatePassword();
        Employee employee = new Employee();
        employee.setFirstname(addEmployeeDto.firstname());
        employee.setLastname(addEmployeeDto.lastname());
        employee.setEmail(addEmployeeDto.email());
        employee.setPassword(passwordEncoder.encode(generatedPassword));
        employee.setPhoneNumber(addEmployeeDto.phoneNumber());
        employee.setRoles(Set.of(employeeRole));
        employee.setHireDate(LocalDateTime.now());
        employee.setEnabled(true);
        employee.setMustChangePassword(true);
        employeeRepository.save(employee);
        return new EmployeeCredentialsDto(employee.getFirstname(), employee.getLastname(), employee.getEmail(), generatedPassword);
    }


}
