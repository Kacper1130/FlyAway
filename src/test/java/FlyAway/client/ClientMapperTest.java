package FlyAway.client;

import FlyAway.client.dto.ClientDto;
import FlyAway.client.dto.ClientReservationDto;
import FlyAway.reservation.Reservation;
import FlyAway.client.dto.ClientRegistrationDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    private ClientMapper userMapper = Mappers.getMapper(ClientMapper.class);

    @Test
    void testMapUserToUserDto() {
        Client client = new Client(
                1L,
                "Steve",
                "Anderson",
                "SAnderson@gmail.com",
                "password123",
                "123456789",
                LocalDate.of(2000, Month.JANUARY, 20),
                new ArrayList<Reservation>(),
                new HashSet<>(),
                false
        );

        ClientDto userDto = userMapper.clientToClientDto(client);

        assertEquals(client.getFirstname(),userDto.firstname());
        assertEquals(client.getLastname(),userDto.lastname());
        assertEquals(client.getEmail(),userDto.email());
        assertEquals(client.getPhoneNumber(),userDto.phoneNumber());
        assertEquals(client.getDayOfBirth(),userDto.dayOfBirth());
    }

    @Test
    void testMapUserRegistrationDtoToUser() {
        ClientRegistrationDto userRegistrationDto = new ClientRegistrationDto(
                "John",
                "Smith",
                "john.smith@gmail.com",
                "secret-password",
                "123456789",
                LocalDate.of(2003,Month.DECEMBER,12)
        );

        Client client = userMapper.clientRegistrationDtoToClient(userRegistrationDto);

        assertNull(client.getId());
        assertEquals(userRegistrationDto.firstname(), client.getFirstname());
        assertEquals(userRegistrationDto.lastname(), client.getLastname());
        assertEquals(userRegistrationDto.email(), client.getEmail());
        assertEquals(userRegistrationDto.password(), client.getPassword());
        assertEquals(userRegistrationDto.phoneNumber(), client.getPhoneNumber());
        assertEquals(userRegistrationDto.dayOfBirth(), client.getDayOfBirth());
        assertNull(client.getReservations());
        assertNull(client.getRoles());
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

        Client client = new Client(
                1L,
                "Steve",
                "Anderson",
                "SAnderson@gmail.com",
                "password123",
                "123456789",
                LocalDate.of(2000, Month.JANUARY, 20),
                List.of(reservation1, reservation2),
                new HashSet<>(),
                false
        );

        ClientReservationDto userReservationDto = userMapper.clientToClientReservationDto(client);

        assertEquals(client.getFirstname(),userReservationDto.firstname());
        assertEquals(client.getLastname(),userReservationDto.lastname());
        assertEquals(client.getEmail(),userReservationDto.email());
        assertEquals(client.getPhoneNumber(),userReservationDto.phoneNumber());
        assertEquals(client.getDayOfBirth(),userReservationDto.dayOfBirth());
        assertEquals(2, userReservationDto.reservations().size());
        assertEquals(18L, userReservationDto.reservations().get(0).seatNumber());
        assertEquals(19L, userReservationDto.reservations().get(1).seatNumber());
    }

}