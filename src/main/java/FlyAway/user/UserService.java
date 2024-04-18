package FlyAway.user;

import FlyAway.exceptions.EmailExistsException;
import FlyAway.exceptions.ReservationDoesNotExistException;
import FlyAway.exceptions.UserDoesNotExistException;
import FlyAway.exceptions.UserDoesNotMatchReservationUserException;
import FlyAway.flight.dto.FlightDto;
import FlyAway.reservation.Reservation;
import FlyAway.reservation.ReservationRepository;
import FlyAway.reservation.ReservationService;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationWithoutUserDto;
import FlyAway.security.RoleRepository;
import FlyAway.user.dto.UserDto;
import FlyAway.user.dto.UserRegistrationDto;
import FlyAway.user.dto.UserReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ReservationRepository reservationRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, RoleRepository roleRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<UserDto> getAll(){
        LOGGER.debug("Retrieving all users from repository");
        List<UserDto> users = userRepository.findAll()
                .stream().map(
                        u -> new UserDto(
                                u.getFirstname(),
                                u.getLastname(),
                                u.getEmail(),
                                u.getPhoneNumber(),
                                u.getDayOfBirth()
                        )
                ).collect(Collectors.toList());

                ;
        LOGGER.info("Retrieved {} users from repository", users.size());
        return users;
    }

    public UserDto addUser(UserRegistrationDto userRegistrationDto){
        LOGGER.debug("Adding new user");

        if (userRepository.findUserByEmail(userRegistrationDto.email()) != null) {
            LOGGER.error("User with email {} already exists", userRegistrationDto.email());
            throw new EmailExistsException(userRegistrationDto.email());
        }

        User createdUser = new User();
        createdUser.setFirstname(userRegistrationDto.firstname());
        createdUser.setLastname(userRegistrationDto.lastname());
        createdUser.setEmail(userRegistrationDto.email());
        createdUser.setPassword(userRegistrationDto.password());
        createdUser.setPhoneNumber(userRegistrationDto.phoneNumber());
        createdUser.setDayOfBirth(userRegistrationDto.dayOfBirth());
        var role = roleRepository.findByName("ROLE_USER");
        createdUser.setRoles(Set.of(role));
        userRepository.save(createdUser);
        LOGGER.info("Created new user with id {}", createdUser.getId());

        UserDto createdUserDto = new UserDto(
                createdUser.getFirstname(),
                createdUser.getLastname(),
                createdUser.getEmail(),
                createdUser.getPhoneNumber(),
                createdUser.getDayOfBirth()
        );
        return createdUserDto;
    }

    public UserDto getUser(Long id) {
        LOGGER.debug("Retrieving user with id {}",id);
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(
                u -> {
                    LOGGER.info("Successfully retrieved user with id {}",id);
                    return new UserDto(
                            u.getFirstname(),
                            u.getLastname(),
                            u.getEmail(),
                            u.getPhoneNumber(),
                            u.getDayOfBirth()
                    );
                }
        ).orElseThrow(() -> {
           LOGGER.error("User with id {} does not exist", id);
           throw new UserDoesNotExistException(id);
        });
    }

    public UserReservationDto getUserWithReservations(Long id) {        //TODO refactor this function
        LOGGER.debug("Retrieving user with reservations, user id {} ", id);
        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.map(
                u -> {
                    LOGGER.info("Successfully retrieved user with reservations, user id {}", id);
                    return new UserReservationDto(
                            u.getFirstname(),
                            u.getLastname(),
                            u.getEmail(),
                            u.getPhoneNumber(),
                            u.getDayOfBirth(),
                            u.getReservations()
                                    .stream().map(
                                            r -> new ReservationWithoutUserDto(
                                                    r.getReservationDate(),
                                                    r.getPrice(),
                                                    r.getSeatNumber(),
                                                    r.getCancelled(),
                                                    new FlightDto(
                                                            r.getFlight().getDepartureCity(),
                                                            r.getFlight().getArrivalCity(),
                                                            r.getFlight().getDepartureDate(),
                                                            r.getFlight().getArrivalDate(),
                                                            r.getFlight().getAirline()
                                                    )
                                            )
                                    ).collect(Collectors.toList())
                    );
                }
        ).orElseThrow(() -> {
            LOGGER.error("User with id {} does not exist", id);
            throw new UserDoesNotExistException(id);
        });
    }

    public ReservationDto getUserReservation(Long userId, UUID reservationId) {
        LOGGER.debug("Retrieving user reservation, user id {}, reservation id {}",userId, reservationId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()){
            LOGGER.info("Successfully retrieved user with id {}",userId);
            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
            if (optionalReservation.isPresent()) {
                LOGGER.info("Successfully retrieved reservation with id {}",reservationId);
                if (!optionalUser.get().equals(optionalReservation.get().getUser())) {
                    LOGGER.warn("User does not match with reservation user");
                    throw new UserDoesNotMatchReservationUserException();
                } else {
                    Reservation r = optionalReservation.get();
                    return new ReservationDto(
                            r.getReservationDate(),
                            r.getPrice(),
                            r.getSeatNumber(),
                            r.getCancelled(),
                            new UserDto(
                                    r.getUser().getFirstname(),
                                    r.getUser().getLastname(),
                                    r.getUser().getEmail(),
                                    r.getUser().getPhoneNumber(),
                                    r.getUser().getDayOfBirth()
                            ),
                            new FlightDto(
                                    r.getFlight().getDepartureCity(),
                                    r.getFlight().getArrivalCity(),
                                    r.getFlight().getDepartureDate(),
                                    r.getFlight().getArrivalDate(),
                                    r.getFlight().getAirline()
                            )
                    );
                }
            } else {
                LOGGER.error("Reservation with id {} does not exist",reservationId);
                throw new ReservationDoesNotExistException(reservationId);
            }
        } else {
            LOGGER.error("User with id {} does not exist", userId);
            throw new UserDoesNotExistException(userId);
        }
    }

    public void cancelReservation(Long userId, UUID reservationId) {

    }


    //TODO delete reservations first
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
}
