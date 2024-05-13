package FlyAway.user;

import FlyAway.exception.ReservationDoesNotExistException;
import FlyAway.exception.UserDoesNotExistException;
import FlyAway.exception.UserDoesNotMatchReservationUserException;
import FlyAway.flight.dto.FlightDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationWithoutUserDto;
import FlyAway.user.dto.UserDto;
import FlyAway.user.dto.UserRegistrationDto;
import FlyAway.user.dto.UserReservationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    void testGetAll() throws Exception {
        UserDto user1 = new UserDto(
                "user1",
                "user1",
                "user1@gmail.com",
                "532235213",
                LocalDate.of(2000, 5, 7)
        );

        UserDto user2 = new UserDto(
                "user2",
                "user2",
                "user2@gmail.com",
                "122235213",
                LocalDate.of(2000, 5, 8)
        );


        List<UserDto> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].firstname").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].lastname").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].email").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].phoneNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].dayOfBirth").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(user1.email()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dayOfBirth")
                        .value(DateTimeFormatter.ISO_LOCAL_DATE.format(user2.dayOfBirth())));

        verify(userService, times(1)).getAll();

    }

    @Test
    void testGetAllWhenNoneExists() throws Exception {

        when(userService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    void testAdd() throws Exception {

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(
                "user1",
                "user1",
                "user1@gmail.com",
                "password1!A",
                "532235213",
                LocalDate.of(2000, 5, 7)
        );

        UserDto userDto = new UserDto(
                userRegistrationDto.firstname(),
                userRegistrationDto.lastname(),
                userRegistrationDto.email(),
                userRegistrationDto.phoneNumber(),
                userRegistrationDto.dayOfBirth()
        );

        when(userService.addUser(userRegistrationDto)).thenReturn(userDto);

        mockMvc.perform(post("/api/v1/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dayOfBirth").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userRegistrationDto.email()));

        verify(userService, times(1)).addUser(userRegistrationDto);

    }

    @Test
    void testAddWhenInvalidUserRegistrationDto() throws Exception {

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(
                "user1",
                "",
                "user1@gmail.com",
                "password1!A",
                "532235213",
                LocalDate.of(2000, 5, 7)
        );

        mockMvc.perform(post("/api/v1/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()));


    }

    @Test
    void testGetUser() throws Exception {

        UserDto userDto = new UserDto(
                "user1",
                "user1",
                "user1@gmail.com",
                "532235213",
                LocalDate.of(2000, 5, 7)
        );

        when(userService.getUser(1L)).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dayOfBirth").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDto.email()));

        verify(userService, times(1)).getUser(1L);

    }

    @Test
    void testGetUserWhenUserIdDoesNotExist() throws Exception {

        when(userService.getUser(2L)).thenThrow(new UserDoesNotExistException());

        mockMvc.perform(get("/api/v1/users/2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertInstanceOf(UserDoesNotExistException.class, result.getResolvedException()));

    }

    @Test
    void testGetUserWithReservations() throws Exception {

        ReservationWithoutUserDto reservationWithoutUserDto1 = new ReservationWithoutUserDto(
                LocalDateTime.of(2024, 5, 7, 16, 30),
                200L,
                15L,
                false,
                new FlightDto(
                        "Cracow",
                        "Warsaw",
                        LocalDateTime.of(2024, 5, 8, 20, 30),
                        LocalDateTime.of(2024, 5, 8, 21, 30),
                        "Ryanair"
                )

        );

        ReservationWithoutUserDto reservationWithoutUserDto2 = new ReservationWithoutUserDto(
                LocalDateTime.of(2024, 5, 7, 16, 40),
                400L,
                15L,
                false,
                new FlightDto(
                        "Cracow",
                        "Paris",
                        LocalDateTime.of(2024, 5, 15, 11, 30),
                        LocalDateTime.of(2024, 5, 15, 14, 0),
                        "Ryanair"
                )

        );

        UserReservationDto userReservationDto = new UserReservationDto(
                "user1",
                "user1",
                "user1@gmail.com",
                "532235213",
                LocalDate.of(2000, 5, 7),
                List.of(reservationWithoutUserDto1, reservationWithoutUserDto2)
        );

        when(userService.getUserWithReservations(1L)).thenReturn(userReservationDto);

        mockMvc.perform(get("/api/v1/users/1/reservations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dayOfBirth").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservations").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservations").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userReservationDto.email()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservations[1].seatNumber").value(15L));

        verify(userService, times(1)).getUserWithReservations(1L);
    }

    @Test
    void testGetUserWithReservationsWhenUserIdDoesNotExist() throws Exception {

        when(userService.getUserWithReservations(2L)).thenThrow(new UserDoesNotExistException());

        mockMvc.perform(get("/api/v1/users/2/reservations"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertInstanceOf(UserDoesNotExistException.class, result.getResolvedException()));

    }

    @Test
    void testGetUserReservation() throws Exception {

        UUID reservationId = UUID.randomUUID();

        UserDto userDto = new UserDto(
                "user1",
                "user1",
                "user1@gmail.com",
                "532235213",
                LocalDate.of(2000, 5, 2)
        );

        FlightDto flightDto = new FlightDto(
                "Cracow",
                "Warsaw",
                LocalDateTime.of(2024, 5, 5, 10, 15),
                LocalDateTime.of(2024, 5, 5, 11, 15),
                "Ryanair"
        );

        ReservationDto reservationDto = new ReservationDto(
                LocalDateTime.of(2024, 5, 7, 16, 30),
                200L,
                15L,
                false,
                userDto,
                flightDto
        );

        when(userService.getUserReservation(1L, reservationId)).thenReturn(reservationDto);

        mockMvc.perform(get("/api/v1/users/1/reservations/" + reservationId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationDate").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.seatNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cancelled").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userDto").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.flightDto").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.seatNumber").value(reservationDto.seatNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userDto.email").value(userDto.email()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flightDto.airline").value(flightDto.airline()));

        verify(userService, times(1)).getUserReservation(1L, reservationId);
    }

    @Test
    void testGetUserReservationWhenUserDoesNotExist() throws Exception {

        UUID reservationId = UUID.randomUUID();

        when(userService.getUserReservation(1L, reservationId)).thenThrow(new UserDoesNotExistException());

        mockMvc.perform(get("/api/v1/users/1/reservations/" + reservationId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertInstanceOf(UserDoesNotExistException.class, result.getResolvedException()));


    }

    @Test
    void testGetUserReservationWhenReservationDoesNotExist() throws Exception {

        UUID reservationId = UUID.randomUUID();

        when(userService.getUserReservation(1L, reservationId)).thenThrow(new ReservationDoesNotExistException());

        mockMvc.perform(get("/api/v1/users/1/reservations/" + reservationId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertInstanceOf(ReservationDoesNotExistException.class, result.getResolvedException()));


    }

    @Test
    void testGetUserReservationWhenUserDoesNotMatchReservationUser() throws Exception {

        UUID reservationId = UUID.randomUUID();

        when(userService.getUserReservation(1L, reservationId)).thenThrow(new UserDoesNotMatchReservationUserException("User does not have access to this reservation"));

        mockMvc.perform(get("/api/v1/users/1/reservations/" + reservationId))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(result -> assertInstanceOf(UserDoesNotMatchReservationUserException.class, result.getResolvedException()));

    }

    @Test
    void testCancelReservation() throws Exception {

        UUID reservationId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/users/1/reservations/" + reservationId + "/cancel"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Cancelled reservation"));

        verify(userService, times(1)).cancelReservation(1L, reservationId);
    }

    @Test
    void testCancelReservationWhenUserDoesNotExist() throws Exception {

        UUID reservationId = UUID.randomUUID();

        doThrow(new UserDoesNotExistException())
                .when(userService).cancelReservation(1L, reservationId);

        mockMvc.perform(delete("/api/v1/users/1/reservations/" + reservationId + "/cancel"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertInstanceOf(UserDoesNotExistException.class, result.getResolvedException()));


    }

    @Test
    void testCancelReservationWhenReservationDoesNotExist() throws Exception {

        UUID reservationId = UUID.randomUUID();

        doThrow(new ReservationDoesNotExistException())
                .when(userService).cancelReservation(1L, reservationId);


        mockMvc.perform(delete("/api/v1/users/1/reservations/" + reservationId + "/cancel"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertInstanceOf(ReservationDoesNotExistException.class, result.getResolvedException()));

    }

    @Test
    void testCancelReservationWhenUserDoesNotMatchReservationUser() throws Exception {

        UUID reservationId = UUID.randomUUID();

        doThrow(new UserDoesNotMatchReservationUserException())
                .when(userService).cancelReservation(1L, reservationId);

        mockMvc.perform(delete("/api/v1/users/1/reservations/" + reservationId + "/cancel"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(result -> assertInstanceOf(UserDoesNotMatchReservationUserException.class, result.getResolvedException()));

    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/api/v1/users/" + userId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);

    }

    @Test
    void testDeleteUserWhenUserDoesNotExist() throws Exception {
        Long userId = 2L;

        doThrow(new UserDoesNotExistException()).when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/users/" + userId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertInstanceOf(UserDoesNotExistException.class, result.getResolvedException()));

    }


}