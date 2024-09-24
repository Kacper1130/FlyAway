package FlyAway.admin;

import FlyAway.employee.Employee;
import FlyAway.employee.EmployeeService;
import FlyAway.employee.dto.AddEmployeeDto;
import FlyAway.employee.dto.EmployeeCredentialsDto;
import FlyAway.exception.EmailExistsException;
import FlyAway.role.RoleRepository;
import FlyAway.user.UserRepository;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AdminService {

    private final EmployeeService employeeService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(EmployeeService employeeService, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeCredentialsDto createEmployee(AddEmployeeDto addEmployeeDto) {
        if (userRepository.existsByEmail(addEmployeeDto.email())) {
            throw new EmailExistsException(addEmployeeDto.email());
        }

        var employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new IllegalStateException("ROLE EMPLOYEE was not initialized"));

        String generatedPassword = generatePassword();
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
        employeeService.addEmployee(employee);
        return new EmployeeCredentialsDto(employee.getFirstname(), employee.getLastname(), employee.getEmail(), generatedPassword);
    }

    private String generatePassword() {
        PasswordGenerator gen = new PasswordGenerator();

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(1);

        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(1);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(1);

        CharacterRule specialCharRule = new CharacterRule(new CharacterData() {
            @Override
            public String getErrorCode() {
                return "SAMPLE_ERROR_CODE";
            }

            @Override
            public String getCharacters() {
                return "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
            }
        });
        specialCharRule.setNumberOfCharacters(1);

        String password = gen.generatePassword(15,upperCaseRule, lowerCaseRule, digitRule, specialCharRule);
        return password;
    }
}
