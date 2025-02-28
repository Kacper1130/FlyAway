package FlyAway.employee;

import FlyAway.WithMockAdmin;
import FlyAway.employee.dto.AddEmployeeDto;
import FlyAway.employee.dto.DisplayEmployeeDto;
import FlyAway.employee.dto.EmployeeCredentialsDto;
import FlyAway.exception.EmailExistsException;
import FlyAway.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(AdminEmployeeController.class)
@WithMockAdmin
class AdminEmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private JwtService jwtService;


    @Test
    void getAllEmployees_WhenEmployeesExist_ShouldReturnEmployeeList() throws Exception {
        Faker faker = new Faker();

        var firstname1 = faker.name().firstName();
        var lastname1 = faker.name().lastName();

        DisplayEmployeeDto employee1 = new DisplayEmployeeDto(
                firstname1,
                lastname1,
                "employee1@flyaway.com",
                faker.phoneNumber().phoneNumber(),
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now().minusHours(1)
        );

        var firstname2 = faker.name().firstName();
        var lastname2 = faker.name().lastName();

        DisplayEmployeeDto employee2 = new DisplayEmployeeDto(
                firstname2,
                lastname2,
                "employee2@flyaway.com",
                faker.phoneNumber().phoneNumber(),
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now().minusHours(1)
        );

        when(employeeService.getAll()).thenReturn(List.of(employee1, employee2));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstname").value(firstname1))
                .andExpect(jsonPath("$[0].lastname").value(lastname1))
                .andExpect(jsonPath("$[0].email").value("employee1@flyaway.com"))
                .andExpect(jsonPath("$[1].firstname").value(firstname2))
                .andExpect(jsonPath("$[1].lastname").value(lastname2))
                .andExpect(jsonPath("$[1].email").value("employee2@flyaway.com"));

        verify(employeeService, times(1)).getAll();
    }

    @Test
    void getAllEmployees_WhenNoEmployeesExist_ShouldReturnEmptyList() throws Exception {
        when(employeeService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(employeeService, times(1)).getAll();
    }

    @Test
    void createEmployee_WhenValidRequest_ShouldCreateEmployee() throws Exception {
        Faker faker = new Faker();

        var firstname = faker.name().firstName();
        var lastname = faker.name().lastName();

        AddEmployeeDto addEmployeeDto = new AddEmployeeDto(
                firstname,
                lastname,
                "employee@flyaway.com",
                "123456789"
        );

        when(employeeService.createEmployee(addEmployeeDto)).thenReturn(new EmployeeCredentialsDto(
                firstname, lastname, "employee@flyaway.com", "password123"));

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addEmployeeDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname").value(firstname))
                .andExpect(jsonPath("$.lastname").value(lastname))
                .andExpect(jsonPath("$.email").value("employee@flyaway.com"))
                .andExpect(jsonPath("$.password").value("password123"));

        verify(employeeService, times(1)).createEmployee(addEmployeeDto);
    }


    @Test
    void createEmployee_WhenMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        Faker faker = new Faker();

        var firstname = faker.name().firstName();
        var lastname = faker.name().lastName();

        AddEmployeeDto addEmployeeDto = new AddEmployeeDto(
                firstname,
                lastname,
                null,
                "123456789"
        );

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addEmployeeDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).createEmployee(any());
    }

    @Test
    void createEmployee_WhenEmailAlreadyExists_ShouldReturnConflict() throws Exception {
        Faker faker = new Faker();
        var firstname = faker.name().firstName();
        var lastname = faker.name().lastName();

        AddEmployeeDto addEmployeeDto = new AddEmployeeDto(
                firstname,
                lastname,
                "existing@flyaway.com",
                "123456789"
        );

        when(employeeService.createEmployee(addEmployeeDto)).thenThrow(new EmailExistsException(addEmployeeDto.email()));

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addEmployeeDto))
                        .with(csrf()))
                .andExpect(status().isConflict());

        verify(employeeService, times(1)).createEmployee(any());
    }


}