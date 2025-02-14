package FlyAway.reservation;

import FlyAway.exception.FlightDoesNotExistException;
import FlyAway.exception.ReservationDoesNotExistException;
import FlyAway.exception.UserDoesNotExistException;
import FlyAway.flight.Flight;
import FlyAway.flight.FlightRepository;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.client.Client;
import FlyAway.client.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ReservationServiceTest {

    @InjectMocks
    ClientReservationService reservationService;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ClientRepository userRepository;

    @Mock
    FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllWhenReservationsExist() {
        Reservation reservation1 = new Reservation();
        reservation1.setId(UUID.randomUUID());
        reservation1.setReservationDate(LocalDateTime.of(2024, Month.APRIL, 30,14,30));

        Reservation reservation2 = new Reservation();
        reservation2.setId(UUID.randomUUID());
        reservation2.setReservationDate(LocalDateTime.of(2024, Month.APRIL, 30,15,30));

        List<Reservation> mockReservations = new ArrayList<>();
        mockReservations.add(reservation1);
        mockReservations.add(reservation2);

        when(reservationRepository.findAll()).thenReturn(mockReservations);

        List<DisplayReservationDto> reservations = reservationService.getAll();

        assertEquals(mockReservations.size(), reservations.size());
        assertEquals(mockReservations.get(0).getId(), reservations.get(0).id());
        assertEquals(mockReservations.get(1).getReservationDate(), reservations.get(1).reservationDate());
        verify(reservationRepository, times(1)).findAll();

    }

    @Test
    void testGetAllWhenNoneExists() {
        when(reservationRepository.findAll()).thenReturn(new ArrayList<>());

        List<DisplayReservationDto> reservations = reservationService.getAll();

        assertEquals(0, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testAddReservationSuccessful() {
        Long userId = 1L;
        UUID flightId = UUID.randomUUID();

        Client client = new Client();
        client.setId(userId);
        client.setEmail("testuser@gmail.com");
        client.setReservations(new ArrayList<>());

        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setAirline("Ryanair");
        flight.setReservations(new ArrayList<>());

        CreateReservationDto createReservationDto = new CreateReservationDto(
                150L,
                15L,
                userId,
                flightId
        );


        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                LocalDateTime.now(),
                150L,
                15L,
                false,
                client,
                flight
        );


        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(client));
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationDto reservationDto = reservationService.createReservation(createReservationDto);

        assertEquals(reservation.getSeatNumber(), reservationDto.seatNumber());
        assertEquals(client.getEmail(), reservationDto.userDto().email());
        assertEquals(flight.getAirline(), reservationDto.flightDto().airline());
        verify(userRepository, times(1)).findActiveById(userId);
        verify(flightRepository, times(1)).findById(flightId);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testAddReservationUserDoesNotExist() {
        Long userId = 2L;
        UUID flightId = UUID.randomUUID();

        CreateReservationDto createReservationDto = new CreateReservationDto(
                150L,
                15L,
                userId,
                flightId
        );

        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class, () -> reservationService.createReservation(createReservationDto));
        verify(userRepository, times(1)).findActiveById(userId);
        verify(flightRepository, times(0)).findById(any(UUID.class));
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }

    @Test
    void testAddReservationFlightDoesNotExist() {
        Long userId = 1L;
        UUID flightId = UUID.randomUUID();

        Client client = new Client();
        client.setId(userId);

        CreateReservationDto createReservationDto = new CreateReservationDto(
                150L,
                15L,
                userId,
                flightId
        );

        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(client));
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        assertThrows(FlightDoesNotExistException.class, () -> reservationService.createReservation(createReservationDto));
        verify(userRepository, times(1)).findActiveById(userId);
        verify(flightRepository, times(1)).findById(flightId);
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }

    @Test
    void testGetReservation() {
        UUID reservationId = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setSeatNumber(15L);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        ReservationDto result = reservationService.getReservation(reservationId);

        assertNotNull(result);
        assertEquals(reservation.getSeatNumber(), result.seatNumber());
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testGetReservation_NotFound() {
        UUID reservationId = UUID.randomUUID();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationDoesNotExistException.class, () -> reservationService.getReservation(reservationId));

        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testCancelReservation_Success() {
        UUID reservationId = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setCancelled(false);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(reservationId);

        assertTrue(reservation.isCancelled());
        verify(reservationRepository, times(1)).findById(reservationId);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testCancelReservation_NotFound() {
        UUID reservationId = UUID.randomUUID();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationDoesNotExistException.class, () -> reservationService.cancelReservation(reservationId));

        verify(reservationRepository, times(1)).findById(reservationId);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

}
