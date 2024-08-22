package FlyAway.user;

import FlyAway.exception.EmailExistsException;
import FlyAway.exception.ReservationDoesNotExistException;
import FlyAway.exception.UserDoesNotExistException;
import FlyAway.exception.UserDoesNotMatchReservationUserException;
import FlyAway.flight.Flight;
import FlyAway.reservation.Reservation;
import FlyAway.reservation.ReservationRepository;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.role.Role;
import FlyAway.role.RoleRepository;
import FlyAway.user.dto.UserDto;
import FlyAway.user.dto.UserRegistrationDto;
import FlyAway.user.dto.UserReservationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllWhenUsersExist() {
        User user1 = new User(
                1L,
                "John",
                "Smith",
                "jsmth@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.FEBRUARY, 11),
                new ArrayList<>(),
                new HashSet<>(),
                false
        );

        User user2 = new User(
                1L,
                "Thomas",
                "Anderson",
                "thomas123@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<>(),
                new HashSet<>(),
                false
        );

        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(user1);
        mockUsers.add(user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserDto> users = userService.getAll();

        assertEquals(mockUsers.size(), users.size());
        assertEquals(mockUsers.get(0).getFirstname(), users.get(0).firstname());
        assertEquals(mockUsers.get(1).getEmail(), users.get(1).email());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllWhenNoneExist() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        List<UserDto> users = userService.getAll();

        assertEquals(0, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testAddUser() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(
                "Thomas",
                "Anderson",
                "thomas123@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11)
        );

        UserDto expectedUserDto = new UserDto(
                userRegistrationDto.firstname(),
                userRegistrationDto.lastname(),
                userRegistrationDto.email(),
                userRegistrationDto.phoneNumber(),
                userRegistrationDto.dayOfBirth()
        );

        when(roleRepository.findByName("ROLE_USER")).thenReturn(new Role(1L, "ROLE_USER"));

        UserDto userDto = userService.addUser(userRegistrationDto);

        assertNotNull(userDto);
        assertEquals(expectedUserDto.firstname(),userDto.firstname());
        assertEquals(expectedUserDto.email(),userDto.email());
        verify(userRepository,times(1)).save((any()));

    }

    @Test
    void testAddUserWhenEmailExists() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11)
        );

        when(userRepository.findUserByEmail(userRegistrationDto.email())).thenReturn(new User());

        assertThrows(EmailExistsException.class, () -> userService.addUser(userRegistrationDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserWhenUserExists() {

        Long userId = 1L;

        User mockUser = new User(
                1L,
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<Reservation>(),
                new HashSet<Role>(),
                false
        );

        when(userRepository.findActiveById(userId))
                .thenReturn(Optional.of(mockUser));

        UserDto userDto = userService.getUser(userId);

        assertNotNull(userDto);
        assertEquals(mockUser.getFirstname(), userDto.firstname());
        assertEquals(mockUser.getEmail(), userDto.email());
        verify(userRepository, times(1)).findActiveById(userId);
    }

    @Test
    void testGetUserWhenUserDoesNotExist() {

        Long userId = 2L;

        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class, () -> userService.getUser(userId));
        verify(userRepository, times(1)).findActiveById(userId);
    }

    @Test
    void testGetUserWithReservationsWhenUserExists() {
        Long userId = 1L;

        User mockUser = new User(
                1L,
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<Reservation>(List.of(new Reservation())),
                new HashSet<Role>(),
                false
        );

        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockUser));

        UserReservationDto userReservationDto = userService.getUserWithReservations(userId);

        assertNotNull(userReservationDto);
        assertEquals(mockUser.getFirstname(), userReservationDto.firstname());
        assertEquals(mockUser.getEmail(), userReservationDto.email());
        assertEquals(1, userReservationDto.reservations().size());
        verify(userRepository, times(1)).findActiveById(userId);
    }

    @Test
    void testGetUserWithReservationsWhenUserDoesNotExist() {

        Long userId = 1L;

        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class, () -> userService.getUserWithReservations(userId));
        verify(userRepository, times(1)).findActiveById(userId);
    }

    @Test
    void testGetUserReservation() {

        Long userId = 1L;
        UUID reservationId = UUID.randomUUID();

        User mockUser = new User(
                1L,
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<Reservation>(),
                new HashSet<Role>(),
                false
        );

        Reservation mockReservation = new Reservation(
                reservationId,
                LocalDateTime.of(2024,Month.APRIL,24,19,30),
                200L,
                15L,
                false,
                mockUser,
                new Flight()
        );

        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockUser));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));
        System.out.println(mockUser);
        ReservationDto reservationDto = userService.getUserReservation(userId, reservationId);

        assertNotNull(reservationDto);
        assertEquals(mockReservation.getReservationDate(), reservationDto.reservationDate());
        assertEquals(mockReservation.getUser().getEmail(), reservationDto.userDto().email());
        verify(userRepository,times(1)).findActiveById(userId);
        verify(reservationRepository,times(1)).findById(reservationId);
    }

    @Test
    void testGetUserReservationWhenUserDoesNotExist() {

        Long userId = 2L;
        UUID reservationId = UUID.randomUUID();

        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class, () -> userService.getUserReservation(userId,reservationId));
        verify(userRepository,times(1)).findActiveById(userId);
    }

    @Test
    void testGetUserReservationWhenReservationDoesNotExist() {

        Long userId = 2L;
        UUID reservationId = UUID.randomUUID();

        User mockUser = new User(
                1L,
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<Reservation>(),
                new HashSet<Role>(),
                false
        );

        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockUser));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationDoesNotExistException.class, () -> userService.getUserReservation(userId,reservationId));
        verify(userRepository,times(1)).findActiveById(userId);
        verify(reservationRepository,times(1)).findById(reservationId);
    }

    @Test
    void testGetUserReservationWhenUserDoesNotMatchReservationUser() {
        Long userId = 1L;
        UUID reservationId = UUID.randomUUID();

        User mockUser = new User(
                1L,
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<Reservation>(),
                new HashSet<Role>(),
                false
        );

        Reservation mockReservation = new Reservation(
                reservationId,
                LocalDateTime.of(2024,Month.APRIL,24,19,30),
                200L,
                15L,
                false,
                new User(),
                new Flight()
        );

        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockUser));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));

        assertThrows(UserDoesNotMatchReservationUserException.class, () -> userService.getUserReservation(userId,reservationId));
        verify(userRepository,times(1)).findActiveById(userId);
        verify(reservationRepository,times(1)).findById(reservationId);
    }

    @Test
    void testCancelReservation() {

        Long userId = 1L;
        UUID reservationId = UUID.randomUUID();

        User mockUser = new User(
                1L,
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<Reservation>(),
                new HashSet<Role>(),
                false
        );

        Reservation mockReservation = new Reservation(
                reservationId,
                LocalDateTime.of(2024,Month.APRIL,24,19,30),
                200L,
                15L,
                false,
                mockUser,
                new Flight()
        );

        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockUser));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));

        userService.cancelReservation(userId,reservationId);

        assertTrue(mockReservation.isCancelled());
        verify(userRepository,times(1)).findActiveById(userId);
        verify(reservationRepository,times(1)).findById(reservationId);


    }

    @Test
    void testCancelReservationWhenUserDoesNotExist() {

        Long userId = 2L;
        UUID reservationId = UUID.randomUUID();

        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class, () -> userService.cancelReservation(userId,reservationId));
        verify(userRepository,times(1)).findActiveById(userId);
    }

    @Test
    void testCancelReservationWhenReservationDoesNotExist() {
        Long userId = 2L;
        UUID reservationId = UUID.randomUUID();

        User mockUser = new User(
                1L,
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<Reservation>(),
                new HashSet<Role>(),
                false
        );

        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockUser));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationDoesNotExistException.class, () -> userService.cancelReservation(userId,reservationId));
        verify(userRepository,times(1)).findActiveById(userId);
        verify(reservationRepository,times(1)).findById(reservationId);
    }

    @Test
    void testCancelReservationWhenUserDoesNotMatchReservationUser() {
        Long userId = 1L;
        UUID reservationId = UUID.randomUUID();

        User mockUser = new User(
                1L,
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<Reservation>(),
                new HashSet<Role>(),
                false
        );

        Reservation mockReservation = new Reservation(
                reservationId,
                LocalDateTime.of(2024,Month.APRIL,24,19,30),
                200L,
                15L,
                false,
                new User(),
                new Flight()
        );

        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockUser));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));

        assertThrows(UserDoesNotMatchReservationUserException.class, () -> userService.cancelReservation(userId,reservationId));
        verify(userRepository,times(1)).findActiveById(userId);
        verify(reservationRepository,times(1)).findById(reservationId);
    }

    @Test
    void testDeleteUserWhenUserDoesNotHaveReservations() {

        Long userId = 1L;

        User mockUser = new User(
                1L,
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<Reservation>(),
                new HashSet<Role>(),
                false
        );

        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockUser));

        userService.deleteUser(userId);

        assertTrue(mockUser.isDeleted());
        verify(userRepository, times(1)).findActiveById(userId);
        verify(userRepository, times(1)).save(mockUser);

    }

    @Test
    void testDeleteUserWhenUserHaveReservations() {

        Long userId = 1L;
        UUID reservationId1 = UUID.randomUUID();
        UUID reservationId2 = UUID.randomUUID();

        User mockUser = new User(
                1L,
                "Thomas",
                "Anderson",
                "existingEmail@gmail.com",
                "password",
                "123123123",
                LocalDate.of(2000, Month.OCTOBER, 11),
                new ArrayList<Reservation>(),
                new HashSet<Role>(),
                false
        );

        Reservation mockReservation1 = new Reservation(
                reservationId1,
                LocalDateTime.of(2024,Month.APRIL,24,19,30),
                200L,
                15L,
                false,
                mockUser,
                new Flight()
        );

        Reservation mockReservation2 = new Reservation(
                reservationId2,
                LocalDateTime.of(2024,Month.APRIL,25,19,30),
                200L,
                16L,
                false,
                mockUser,
                new Flight()
        );

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(mockReservation1);
        reservations.add(mockReservation2);
        mockUser.setReservations(reservations);

        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockUser));

        userService.deleteUser(userId);

        assertTrue(mockUser.isDeleted());
        assertTrue(mockReservation1.isCancelled());
        assertTrue(mockReservation2.isCancelled());
        verify(userRepository, times(1)).findActiveById(userId);
        verify(userRepository, times(1)).save(mockUser);

    }

    @Test
    void testDeleteUserWhenUserDoesNotExist() {
        Long userId = 2L;

        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class, () -> userService.deleteUser(userId));
        verify(userRepository,times(1)).findActiveById(userId);
        verify(userRepository, never()).deleteById(any(Long.class));
    }

}