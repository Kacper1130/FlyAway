//package FlyAway.client;
//
//import FlyAway.exception.EmailExistsException;
//import FlyAway.exception.ReservationDoesNotExistException;
//import FlyAway.exception.UserDoesNotExistException;
//import FlyAway.exception.UserDoesNotMatchReservationUserException;
//import FlyAway.flight.Flight;
//import FlyAway.reservation.Reservation;
//import FlyAway.reservation.ReservationRepository;
//import FlyAway.reservation.dto.ReservationDto;
//import FlyAway.role.Role;
//import FlyAway.role.RoleRepository;
//import FlyAway.client.dto.ClientDto;
//import FlyAway.client.dto.ClientRegistrationDto;
//import FlyAway.client.dto.ClientReservationDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ClientServiceTest {
//
//    @InjectMocks
//    ClientService userService;
//
//    @Mock
//    ClientRepository userRepository;
//
//    @Mock
//    RoleRepository roleRepository;
//
//    @Mock
//    ReservationRepository reservationRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllWhenUsersExist() {
//        Client client1 = new Client(
//                1L,
//                "John",
//                "Smith",
//                "jsmth@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.FEBRUARY, 11),
//                new ArrayList<>(),
//                new HashSet<>(),
//                false
//        );
//
//        Client client2 = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "thomas123@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<>(),
//                new HashSet<>(),
//                false
//        );
//
//        List<Client> mockClients = new ArrayList<>();
//        mockClients.add(client1);
//        mockClients.add(client2);
//
//        when(userRepository.findAll()).thenReturn(mockClients);
//
//        List<ClientDto> users = userService.getAll();
//
//        assertEquals(mockClients.size(), users.size());
//        assertEquals(mockClients.get(0).getFirstname(), users.get(0).firstname());
//        assertEquals(mockClients.get(1).getEmail(), users.get(1).email());
//        verify(userRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetAllWhenNoneExist() {
//        when(userRepository.findAll()).thenReturn(new ArrayList<>());
//
//        List<ClientDto> users = userService.getAll();
//
//        assertEquals(0, users.size());
//        verify(userRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testAddUser() {
//        ClientRegistrationDto userRegistrationDto = new ClientRegistrationDto(
//                "Thomas",
//                "Anderson",
//                "thomas123@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11)
//        );
//
//        ClientDto expectedUserDto = new ClientDto(
//                userRegistrationDto.firstname(),
//                userRegistrationDto.lastname(),
//                userRegistrationDto.email(),
//                userRegistrationDto.phoneNumber(),
//                userRegistrationDto.dayOfBirth()
//        );
//
//        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role(1L, "ROLE_USER")));
//
//        ClientDto userDto = userService.addUser(userRegistrationDto);
//
//        assertNotNull(userDto);
//        assertEquals(expectedUserDto.firstname(),userDto.firstname());
//        assertEquals(expectedUserDto.email(),userDto.email());
//        verify(userRepository,times(1)).save((any()));
//
//    }
//
//    @Test
//    void testAddUserWhenEmailExists() {
//        ClientRegistrationDto userRegistrationDto = new ClientRegistrationDto(
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11)
//        );
//
//        when(userRepository.findByEmail(userRegistrationDto.email())).thenReturn(Optional.of(new Client()));
//
//        assertThrows(EmailExistsException.class, () -> userService.addUser(userRegistrationDto));
//        verify(userRepository, never()).save(any(Client.class));
//    }
//
//    @Test
//    void testGetUserWhenUserExists() {
//
//        Long userId = 1L;
//
//        Client mockClient = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<Reservation>(),
//                new HashSet<Role>(),
//                false
//        );
//
//        when(userRepository.findActiveById(userId))
//                .thenReturn(Optional.of(mockClient));
//
//        ClientDto userDto = userService.getUser(userId);
//
//        assertNotNull(userDto);
//        assertEquals(mockClient.getFirstname(), userDto.firstname());
//        assertEquals(mockClient.getEmail(), userDto.email());
//        verify(userRepository, times(1)).findActiveById(userId);
//    }
//
//    @Test
//    void testGetUserWhenUserDoesNotExist() {
//
//        Long userId = 2L;
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserDoesNotExistException.class, () -> userService.getUser(userId));
//        verify(userRepository, times(1)).findActiveById(userId);
//    }
//
//    @Test
//    void testGetUserWithReservationsWhenUserExists() {
//        Long userId = 1L;
//
//        Client mockClient = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<Reservation>(List.of(new Reservation())),
//                new HashSet<Role>(),
//                false
//        );
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockClient));
//
//        ClientReservationDto userReservationDto = userService.getUserWithReservations(userId);
//
//        assertNotNull(userReservationDto);
//        assertEquals(mockClient.getFirstname(), userReservationDto.firstname());
//        assertEquals(mockClient.getEmail(), userReservationDto.email());
//        assertEquals(1, userReservationDto.reservations().size());
//        verify(userRepository, times(1)).findActiveById(userId);
//    }
//
//    @Test
//    void testGetUserWithReservationsWhenUserDoesNotExist() {
//
//        Long userId = 1L;
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserDoesNotExistException.class, () -> userService.getUserWithReservations(userId));
//        verify(userRepository, times(1)).findActiveById(userId);
//    }
//
//    @Test
//    void testGetUserReservation() {
//
//        Long userId = 1L;
//        UUID reservationId = UUID.randomUUID();
//
//        Client mockClient = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<Reservation>(),
//                new HashSet<Role>(),
//                false
//        );
//
//        Reservation mockReservation = new Reservation(
//                reservationId,
//                LocalDateTime.of(2024,Month.APRIL,24,19,30),
//                200L,
//                15L,
//                false,
//                mockClient,
//                new Flight()
//        );
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockClient));
//        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));
//        System.out.println(mockClient);
//        ReservationDto reservationDto = userService.getUserReservation(userId, reservationId);
//
//        assertNotNull(reservationDto);
//        assertEquals(mockReservation.getReservationDate(), reservationDto.reservationDate());
//        assertEquals(mockReservation.getClient().getEmail(), reservationDto.userDto().email());
//        verify(userRepository,times(1)).findActiveById(userId);
//        verify(reservationRepository,times(1)).findById(reservationId);
//    }
//
//    @Test
//    void testGetUserReservationWhenUserDoesNotExist() {
//
//        Long userId = 2L;
//        UUID reservationId = UUID.randomUUID();
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserDoesNotExistException.class, () -> userService.getUserReservation(userId,reservationId));
//        verify(userRepository,times(1)).findActiveById(userId);
//    }
//
//    @Test
//    void testGetUserReservationWhenReservationDoesNotExist() {
//
//        Long userId = 2L;
//        UUID reservationId = UUID.randomUUID();
//
//        Client mockClient = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<Reservation>(),
//                new HashSet<Role>(),
//                false
//        );
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockClient));
//        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());
//
//        assertThrows(ReservationDoesNotExistException.class, () -> userService.getUserReservation(userId,reservationId));
//        verify(userRepository,times(1)).findActiveById(userId);
//        verify(reservationRepository,times(1)).findById(reservationId);
//    }
//
//    @Test
//    void testGetUserReservationWhenUserDoesNotMatchReservationUser() {
//        Long userId = 1L;
//        UUID reservationId = UUID.randomUUID();
//
//        Client mockClient = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<Reservation>(),
//                new HashSet<Role>(),
//                false
//        );
//
//        Reservation mockReservation = new Reservation(
//                reservationId,
//                LocalDateTime.of(2024,Month.APRIL,24,19,30),
//                200L,
//                15L,
//                false,
//                new Client(),
//                new Flight()
//        );
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockClient));
//        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));
//
//        assertThrows(UserDoesNotMatchReservationUserException.class, () -> userService.getUserReservation(userId,reservationId));
//        verify(userRepository,times(1)).findActiveById(userId);
//        verify(reservationRepository,times(1)).findById(reservationId);
//    }
//
//    @Test
//    void testCancelReservation() {
//
//        Long userId = 1L;
//        UUID reservationId = UUID.randomUUID();
//
//        Client mockClient = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<Reservation>(),
//                new HashSet<Role>(),
//                false
//        );
//
//        Reservation mockReservation = new Reservation(
//                reservationId,
//                LocalDateTime.of(2024,Month.APRIL,24,19,30),
//                200L,
//                15L,
//                false,
//                mockClient,
//                new Flight()
//        );
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockClient));
//        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));
//
//        userService.cancelReservation(userId,reservationId);
//
//        assertTrue(mockReservation.isCancelled());
//        verify(userRepository,times(1)).findActiveById(userId);
//        verify(reservationRepository,times(1)).findById(reservationId);
//
//
//    }
//
//    @Test
//    void testCancelReservationWhenUserDoesNotExist() {
//
//        Long userId = 2L;
//        UUID reservationId = UUID.randomUUID();
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserDoesNotExistException.class, () -> userService.cancelReservation(userId,reservationId));
//        verify(userRepository,times(1)).findActiveById(userId);
//    }
//
//    @Test
//    void testCancelReservationWhenReservationDoesNotExist() {
//        Long userId = 2L;
//        UUID reservationId = UUID.randomUUID();
//
//        Client mockClient = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<Reservation>(),
//                new HashSet<Role>(),
//                false
//        );
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockClient));
//        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());
//
//        assertThrows(ReservationDoesNotExistException.class, () -> userService.cancelReservation(userId,reservationId));
//        verify(userRepository,times(1)).findActiveById(userId);
//        verify(reservationRepository,times(1)).findById(reservationId);
//    }
//
//    @Test
//    void testCancelReservationWhenUserDoesNotMatchReservationUser() {
//        Long userId = 1L;
//        UUID reservationId = UUID.randomUUID();
//
//        Client mockClient = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<Reservation>(),
//                new HashSet<Role>(),
//                false
//        );
//
//        Reservation mockReservation = new Reservation(
//                reservationId,
//                LocalDateTime.of(2024,Month.APRIL,24,19,30),
//                200L,
//                15L,
//                false,
//                new Client(),
//                new Flight()
//        );
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockClient));
//        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));
//
//        assertThrows(UserDoesNotMatchReservationUserException.class, () -> userService.cancelReservation(userId,reservationId));
//        verify(userRepository,times(1)).findActiveById(userId);
//        verify(reservationRepository,times(1)).findById(reservationId);
//    }
//
//    @Test
//    void testDeleteUserWhenUserDoesNotHaveReservations() {
//
//        Long userId = 1L;
//
//        Client mockClient = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<Reservation>(),
//                new HashSet<Role>(),
//                false
//        );
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockClient));
//
//        userService.deleteUser(userId);
//
//        assertTrue(mockClient.isDeleted());
//        verify(userRepository, times(1)).findActiveById(userId);
//        verify(userRepository, times(1)).save(mockClient);
//
//    }
//
//    @Test
//    void testDeleteUserWhenUserHaveReservations() {
//
//        Long userId = 1L;
//        UUID reservationId1 = UUID.randomUUID();
//        UUID reservationId2 = UUID.randomUUID();
//
//        Client mockClient = new Client(
//                1L,
//                "Thomas",
//                "Anderson",
//                "existingEmail@gmail.com",
//                "password",
//                "123123123",
//                LocalDate.of(2000, Month.OCTOBER, 11),
//                new ArrayList<Reservation>(),
//                new HashSet<Role>(),
//                false
//        );
//
//        Reservation mockReservation1 = new Reservation(
//                reservationId1,
//                LocalDateTime.of(2024,Month.APRIL,24,19,30),
//                200L,
//                15L,
//                false,
//                mockClient,
//                new Flight()
//        );
//
//        Reservation mockReservation2 = new Reservation(
//                reservationId2,
//                LocalDateTime.of(2024,Month.APRIL,25,19,30),
//                200L,
//                16L,
//                false,
//                mockClient,
//                new Flight()
//        );
//
//        List<Reservation> reservations = new ArrayList<>();
//        reservations.add(mockReservation1);
//        reservations.add(mockReservation2);
//        mockClient.setReservations(reservations);
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.of(mockClient));
//
//        userService.deleteUser(userId);
//
//        assertTrue(mockClient.isDeleted());
//        assertTrue(mockReservation1.isCancelled());
//        assertTrue(mockReservation2.isCancelled());
//        verify(userRepository, times(1)).findActiveById(userId);
//        verify(userRepository, times(1)).save(mockClient);
//
//    }
//
//    @Test
//    void testDeleteUserWhenUserDoesNotExist() {
//        Long userId = 2L;
//
//        when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserDoesNotExistException.class, () -> userService.deleteUser(userId));
//        verify(userRepository,times(1)).findActiveById(userId);
//        verify(userRepository, never()).deleteById(any(Long.class));
//    }
//
//}