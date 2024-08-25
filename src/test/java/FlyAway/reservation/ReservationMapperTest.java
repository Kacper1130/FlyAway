package FlyAway.reservation;

import FlyAway.flight.Flight;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationWithoutUserDto;
import FlyAway.client.Client;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationMapperTest {

    ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);

    @Test
    void testMapReservationToDisplayReservationDto() {
        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                LocalDateTime.of(2024,4,23,17,48),
                100L,
                20L,
                false,
                new Client(1L,null,null,null,null,null,null,null,null,false),
                new Flight(UUID.randomUUID(),null,null,null,null,null,null)
        );

        DisplayReservationDto reservationDto = reservationMapper.reservationToDisplayReservationDto(reservation);

        assertEquals(reservation.getId(), reservationDto.id());
        assertEquals(reservation.getReservationDate(), reservationDto.reservationDate());
        assertEquals(reservation.getPrice(), reservationDto.price());
        assertEquals(reservation.getSeatNumber(), reservationDto.seatNumber());
        assertEquals(reservation.isCancelled(), reservationDto.cancelled());
        assertEquals(reservation.getClient().getId(), reservationDto.userId());
        assertEquals(reservation.getFlight().getId(), reservationDto.flightId());
    }

    @Test
    void testMapReservationToReservationDto() {
        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                LocalDateTime.of(2024, 4, 23, 17, 48),
                100L,
                20L,
                false,
                new Client(1L, "Mac", null, null, null, null, null, null, null,false),
                new Flight(UUID.randomUUID(), "New York", null, null, null, null, null)
        );

        ReservationDto reservationDto = reservationMapper.reservationToReservationDto(reservation);

        assertEquals(reservation.getReservationDate(), reservationDto.reservationDate());
        assertEquals(reservation.getPrice(), reservationDto.price());
        assertEquals(reservation.getSeatNumber(), reservationDto.seatNumber());
        assertEquals(reservation.isCancelled(), reservationDto.cancelled());
        assertNotNull(reservationDto.userDto());
        assertEquals(reservation.getClient().getFirstname(), reservationDto.userDto().firstname());
        assertNotNull(reservationDto.flightDto());
        assertEquals(reservation.getFlight().getDepartureCity(), reservationDto.flightDto().departureCity());
    }
    @Test
    void testMapReservationToReservationWithoutUserDto() {
        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                LocalDateTime.of(2024,4,23,17,48),
                100L,
                20L,
                false,
                new Client(1L,null,null,null,null,null,null,null,null,false),
                new Flight(UUID.randomUUID(),"New York",null,null,null,null,null)
        );

        ReservationWithoutUserDto reservationWithoutUserDto = reservationMapper.reservationToReservationWithoutUserDto(reservation);

        assertEquals(reservation.getReservationDate(), reservationWithoutUserDto.reservationDate());
        assertEquals(reservation.getPrice(), reservationWithoutUserDto.price());
        assertEquals(reservation.getSeatNumber(), reservationWithoutUserDto.seatNumber());
        assertEquals(reservation.isCancelled(), reservationWithoutUserDto.cancelled());
        assertNotNull(reservationWithoutUserDto.flightDto());
        assertEquals(reservation.getFlight().getDepartureCity(), reservationWithoutUserDto.flightDto().departureCity());
    }

}