package FlyAway.employee;

import FlyAway.employee.dto.AddEmployeeDto;
import FlyAway.employee.dto.DisplayEmployeeDto;
import FlyAway.employee.dto.EmployeeCredentialsDto;
import FlyAway.exception.EmailExistsException;
import FlyAway.role.Role;
import FlyAway.security.PasswordService;
import FlyAway.user.User;
import FlyAway.user.dao.UserRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {

    private final UserRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;
    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    public EmployeeService(UserRepository employeeRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordService passwordService) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
    }

    public List<DisplayEmployeeDto> getAll(){
        List<User> employees = employeeRepository.findAllUsersByRole(Role.ROLE_EMPLOYEE);
        return employees.stream()
                .map(employeeMapper::employeeToDisplayEmployeeDto)
                .toList();
    }

    public EmployeeCredentialsDto createEmployee(AddEmployeeDto addEmployeeDto) {
        if (userRepository.existsByEmail(addEmployeeDto.email())) {
            throw new EmailExistsException(addEmployeeDto.email());
        }

        String generatedPassword = passwordService.generatePassword();
        User employee = new User();
        employee.setFirstname(addEmployeeDto.firstname());
        employee.setLastname(addEmployeeDto.lastname());
        employee.setEmail(addEmployeeDto.email());
        employee.setPassword(passwordEncoder.encode(generatedPassword));
        employee.setPhoneNumber(addEmployeeDto.phoneNumber());
        employee.setRole(Role.ROLE_EMPLOYEE);
        employee.setHireDate(LocalDate.now());
        employee.setEnabled(true);
        employee.setLastLogin(null);
        employeeRepository.save(employee);
        return new EmployeeCredentialsDto(employee.getFirstname(), employee.getLastname(), employee.getEmail(), generatedPassword);
    }


}
