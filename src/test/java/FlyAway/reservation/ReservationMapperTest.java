package FlyAway.reservation;

import FlyAway.flight.Flight;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationWithoutUserDto;
import FlyAway.user.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationMapperTest {

    ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);

    @Test
    void shouldMapReservationToDisplayReservationDto() {
        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                LocalDateTime.of(2024,4,23,17,48),
                100L,
                20L,
                false,
                new User(1L,null,null,null,null,null,null,null,null),
                new Flight(UUID.randomUUID(),null,null,null,null,null,null)
        );

        DisplayReservationDto reservationDto = reservationMapper.reservationToDisplayReservationDto(reservation);

        assertEquals(reservation.getId(), reservationDto.id());
        assertEquals(reservation.getReservationDate(), reservationDto.reservationDate());
        assertEquals(reservation.getPrice(), reservationDto.price());
        assertEquals(reservation.getSeatNumber(), reservationDto.seatNumber());
        assertEquals(reservation.getCancelled(), reservationDto.cancelled());
        assertEquals(reservation.getUser().getId(), reservationDto.userId());
        assertEquals(reservation.getFlight().getId(), reservationDto.flightId());
    }

    @Test
    void shouldMapReservationToReservationDto() {
        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                LocalDateTime.of(2024, 4, 23, 17, 48),
                100L,
                20L,
                false,
                new User(1L, "Mac", null, null, null, null, null, null, null),
                new Flight(UUID.randomUUID(), "New York", null, null, null, null, null)
        );

        ReservationDto reservationDto = reservationMapper.reservationToReservationDto(reservation);

        assertEquals(reservation.getReservationDate(), reservationDto.reservationDate());
        assertEquals(reservation.getPrice(), reservationDto.price());
        assertEquals(reservation.getSeatNumber(), reservationDto.seatNumber());
        assertEquals(reservation.getCancelled(), reservationDto.cancelled());
        assertNotNull(reservationDto.userDto());
        assertEquals(reservation.getUser().getFirstname(), reservationDto.userDto().firstname());
        assertNotNull(reservationDto.flightDto());
        assertEquals(reservation.getFlight().getDepartureCity(), reservationDto.flightDto().departureCity());
    }
    @Test
    void shouldMapReservationToReservationWithoutUserDto() {
        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                LocalDateTime.of(2024,4,23,17,48),
                100L,
                20L,
                false,
                new User(1L,null,null,null,null,null,null,null,null),
                new Flight(UUID.randomUUID(),"New York",null,null,null,null,null)
        );

        ReservationWithoutUserDto reservationWithoutUserDto = reservationMapper.reservationToReservationWithoutUserDto(reservation);

        assertEquals(reservation.getReservationDate(), reservationWithoutUserDto.reservationDate());
        assertEquals(reservation.getPrice(), reservationWithoutUserDto.price());
        assertEquals(reservation.getSeatNumber(), reservationWithoutUserDto.seatNumber());
        assertEquals(reservation.getCancelled(), reservationWithoutUserDto.cancelled());
        assertNotNull(reservationWithoutUserDto.flightDto());
        assertEquals(reservation.getFlight().getDepartureCity(), reservationWithoutUserDto.flightDto().departureCity());
    }

}