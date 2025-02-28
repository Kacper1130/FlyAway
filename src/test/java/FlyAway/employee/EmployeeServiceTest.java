package FlyAway.employee;

import FlyAway.employee.dto.AddEmployeeDto;
import FlyAway.employee.dto.DisplayEmployeeDto;
import FlyAway.employee.dto.EmployeeCredentialsDto;
import FlyAway.exception.EmailExistsException;
import FlyAway.exception.RoleNotInitializedException;
import FlyAway.role.Role;
import FlyAway.role.RoleRepository;
import FlyAway.security.PasswordService;
import FlyAway.user.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    private static final Role EMPLOYEE_ROLE;

    static {
        EMPLOYEE_ROLE = new Role();
        EMPLOYEE_ROLE.setName("ROLE_EMPLOYEE");
    }

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private EmployeeService employeeService;


    @Test
    void getAllEmployees_WhenEmployeesExist_ShouldReturnEmployeeList() {
        Faker faker = new Faker();

        var firstname1 = faker.name().firstName();
        var lastname1 = faker.name().lastName();

        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstname(firstname1);
        employee1.setLastname(lastname1);
        employee1.setEmail("employee1@flyaway.com");
        employee1.setPassword("password1");
        employee1.setPhoneNumber("123456789");
        employee1.setRoles(Set.of(EMPLOYEE_ROLE));
        employee1.setLastLogin(LocalDateTime.of(2024,12,12,10,30));
        employee1.setHireDate(LocalDate.of(1990,1,1));

        var firstname2 = faker.name().firstName();
        var lastname2 = faker.name().lastName();

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstname(firstname2);
        employee2.setLastname(lastname2);
        employee2.setEmail("employee2@flyaway.com");
        employee2.setPassword("password2");
        employee2.setPhoneNumber("987654321");
        employee2.setRoles(Set.of(EMPLOYEE_ROLE));
        employee2.setLastLogin(LocalDateTime.of(2024,12,12,11,30));
        employee2.setHireDate(LocalDate.of(1990,2,2));

        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<DisplayEmployeeDto> result = employeeService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(firstname1, result.get(0).firstname());
        assertEquals(lastname1, result.get(0).lastname());
        assertEquals("employee1@flyaway.com", result.get(0).email());
        assertEquals(LocalDateTime.of(2024,12,12,10,30), result.get(0).lastLogin());
        assertEquals(LocalDate.of(1990,1,1), result.get(0).hireDate());
        assertEquals("123456789", result.get(0).phoneNumber());

        assertEquals(firstname2, result.get(1).firstname());
        assertEquals(lastname2, result.get(1).lastname());
        assertEquals("employee2@flyaway.com", result.get(1).email());
        assertEquals(LocalDateTime.of(2024,12,12,11,30), result.get(1).lastLogin());
        assertEquals(LocalDate.of(1990,2,2), result.get(1).hireDate());
        assertEquals("987654321", result.get(1).phoneNumber());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getAllEmployees_WhenNoEmployeesExist_ShouldReturnEmptyList() {
        when(employeeRepository.findAll()).thenReturn(List.of());

        List<DisplayEmployeeDto> result = employeeService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void createEmployee_WhenEmailDoesNotExist_ShouldCreateAndReturnCredentials() {
        Faker faker = new Faker();

        var firstname = faker.name().firstName();
        var lastname = faker.name().lastName();

        AddEmployeeDto addEmployeeDto = new AddEmployeeDto(
                firstname,
                lastname,
                "employee@flyaway.com",
                "123456789"
        );

        when(userRepository.existsByEmail("employee@flyaway.com")).thenReturn(false);
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(Optional.of(EMPLOYEE_ROLE));
        when(passwordService.generatePassword()).thenReturn("generatedPassword");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedGeneratedPassword");

        EmployeeCredentialsDto result = employeeService.createEmployee(addEmployeeDto);

        assertNotNull(result);
        assertEquals(firstname, result.firstname());
        assertEquals(lastname, result.lastname());
        assertEquals("employee@flyaway.com", result.email());
        assertEquals("generatedPassword", result.password());

        verify(userRepository, times(1)).existsByEmail("employee@flyaway.com");
        verify(roleRepository, times(1)).findByName("ROLE_EMPLOYEE");
        verify(passwordService, times(1)).generatePassword();
        verify(passwordEncoder, times(1)).encode("generatedPassword");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_WhenEmailExists_ShouldThrowEmailExistsException() {
        Faker faker = new Faker();

        var firstname = faker.name().firstName();
        var lastname = faker.name().lastName();

        AddEmployeeDto addEmployeeDto = new AddEmployeeDto(
                firstname,
                lastname,
                "employee@flyaway.com",
                "123456789"
        );

        when(userRepository.existsByEmail("employee@flyaway.com")).thenReturn(true);

        assertThrows(EmailExistsException.class, () -> employeeService.createEmployee(addEmployeeDto));

        verify(userRepository, times(1)).existsByEmail("employee@flyaway.com");
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void createEmployee_WhenRoleNotFound_ShouldThrowRoleNotInitializedException() {
        Faker faker = new Faker();

        var firstname = faker.name().firstName();
        var lastname = faker.name().lastName();

        AddEmployeeDto addEmployeeDto = new AddEmployeeDto(
                firstname,
                lastname,
                "employee@flyaway.com",
                "123456789"
        );

        when(userRepository.existsByEmail("employee@flyaway.com")).thenReturn(false);
        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(Optional.empty());

        var exception = assertThrows(RoleNotInitializedException.class, () -> employeeService.createEmployee(addEmployeeDto));

        assertEquals("Role ROLE_EMPLOYEE is not initialized", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail("employee@flyaway.com");
        verify(roleRepository, times(1)).findByName("ROLE_EMPLOYEE");
        verify(employeeRepository, never()).save(any(Employee.class));
    }

}