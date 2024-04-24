package FlyAway.user;

import FlyAway.reservation.Reservation;
import FlyAway.security.Role;
import FlyAway.user.dto.UserDto;
import FlyAway.user.dto.UserRegistrationDto;
import FlyAway.user.dto.UserReservationDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testMapUserToUserDto() {
        User user = new User(
                1L,
                "Steve",
                "Anderson",
                "SAnderson@gmail.com",
                "password123",
                "123456789",
                LocalDate.of(2000, Month.JANUARY, 20),
                new ArrayList<Reservation>(),
                new HashSet<>()
        );

        UserDto userDto = userMapper.userToUserDto(user);

        assertEquals(user.getFirstname(),userDto.firstname());
        assertEquals(user.getLastname(),userDto.lastname());
        assertEquals(user.getEmail(),userDto.email());
        assertEquals(user.getPhoneNumber(),userDto.phoneNumber());
        assertEquals(user.getDayOfBirth(),userDto.dayOfBirth());
    }

    @Test
    void testMapUserRegistrationDtoToUser() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(
                "John",
                "Smith",
                "john.smith@gmail.com",
                "secret-password",
                "123456789",
                LocalDate.of(2003,Month.DECEMBER,12)
        );

        User user = userMapper.userRegistrationDtoToUser(userRegistrationDto);

        assertNull(user.getId());
        assertEquals(userRegistrationDto.firstname(), user.getFirstname());
        assertEquals(userRegistrationDto.lastname(), user.getLastname());
        assertEquals(userRegistrationDto.email(), user.getEmail());
        assertEquals(userRegistrationDto.password(), user.getPassword());
        assertEquals(userRegistrationDto.phoneNumber(), user.getPhoneNumber());
        assertEquals(userRegistrationDto.dayOfBirth(), user.getDayOfBirth());
        assertNull(user.getReservations());
        assertNull(user.getRoles());
    }

    @Test
    void testMapUserToUserReservationDto() {

        Reservation reservation1 = new Reservation(
                UUID.randomUUID(),
                LocalDateTime.of(2024,Month.APRIL,23,11,30),
                450L,
                18L,
                false,
                null,
                null
        );

        Reservation reservation2 = new Reservation(
                UUID.randomUUID(),
                LocalDateTime.of(2024,Month.APRIL,23,11,30),
                450L,
                19L,
                false,
                null,
                null
        );

        User user = new User(
                1L,
                "Steve",
                "Anderson",
                "SAnderson@gmail.com",
                "password123",
                "123456789",
                LocalDate.of(2000, Month.JANUARY, 20),
                List.of(reservation1, reservation2),
                new HashSet<>()
        );

        UserReservationDto userReservationDto = userMapper.userToUserReservationDto(user);

        assertEquals(user.getFirstname(),userReservationDto.firstname());
        assertEquals(user.getLastname(),userReservationDto.lastname());
        assertEquals(user.getEmail(),userReservationDto.email());
        assertEquals(user.getPhoneNumber(),userReservationDto.phoneNumber());
        assertEquals(user.getDayOfBirth(),userReservationDto.dayOfBirth());
        assertEquals(2, userReservationDto.reservations().size());
        assertEquals(18L, userReservationDto.reservations().get(0).seatNumber());
        assertEquals(19L, userReservationDto.reservations().get(1).seatNumber());
    }

}