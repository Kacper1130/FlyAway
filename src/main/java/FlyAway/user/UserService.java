package FlyAway.user;

import FlyAway.exceptions.EmailExistsException;
import FlyAway.exceptions.ReservationDoesNotExistException;
import FlyAway.exceptions.UserDoesNotExistException;
import FlyAway.exceptions.UserDoesNotMatchReservationUserException;
import FlyAway.reservation.Reservation;
import FlyAway.reservation.ReservationMapper;
import FlyAway.reservation.ReservationRepository;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.security.RoleRepository;
import FlyAway.user.dto.UserDto;
import FlyAway.user.dto.UserRegistrationDto;
import FlyAway.user.dto.UserReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public List<UserDto> getAll() {
        LOGGER.debug("Retrieving all users from repository");
        List<UserDto> users = userRepository.findAll()
                .stream().map(UserMapper.INSTANCE::userToUserDto)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} users from repository", users.size());
        return users;
    }

    public UserDto addUser(UserRegistrationDto userRegistrationDto) {
        LOGGER.debug("Adding new user");

        if (userRepository.findUserByEmail(userRegistrationDto.email()) != null) {
            LOGGER.error("User with email {} already exists", userRegistrationDto.email());
            throw new EmailExistsException(userRegistrationDto.email());
        }

        User mappedUser = UserMapper.INSTANCE.userRegistrationDtoToUser(userRegistrationDto);
        var role = roleRepository.findByName("ROLE_USER");
        mappedUser.setRoles(Set.of(role));
        userRepository.save(mappedUser);
        LOGGER.info("Created new user with id {}", mappedUser.getId());

        UserDto createdUserDto = UserMapper.INSTANCE.userToUserDto(mappedUser);
        return createdUserDto;
    }

    public UserDto getUser(Long id) {
        LOGGER.debug("Retrieving user with id {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(
                u -> {
                    LOGGER.info("Successfully retrieved user with id {}", id);
                    return UserMapper.INSTANCE.userToUserDto(u);
                }
        ).orElseThrow(() -> {
            LOGGER.error("User with id {} does not exist", id);
            throw new UserDoesNotExistException(id);
        });
    }

    public UserReservationDto getUserWithReservations(Long id) {
        LOGGER.debug("Retrieving user with reservations, user id {} ", id);
        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.map(
                u -> {
                    LOGGER.info("Successfully retrieved user with reservations, user id {}", id);
                    return UserMapper.INSTANCE.userToUserReservationDto(u);
                }
        ).orElseThrow(() -> {
            LOGGER.error("User with id {} does not exist", id);
            throw new UserDoesNotExistException(id);
        });
    }

    public ReservationDto getUserReservation(Long userId, UUID reservationId) {
        LOGGER.debug("Retrieving user reservation, user id {}, reservation id {}", userId, reservationId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            LOGGER.info("Successfully retrieved user with id {}", userId);
            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
            if (optionalReservation.isPresent()) {
                LOGGER.info("Successfully retrieved reservation with id {}", reservationId);
                if (!optionalUser.get().equals(optionalReservation.get().getUser())) {
                    LOGGER.warn("User does not match with reservation user");
                    throw new UserDoesNotMatchReservationUserException();
                } else {
                    Reservation r = optionalReservation.get();
                    return ReservationMapper.INSTANCE.reservationToReservationDto(r);
                }
            } else {
                LOGGER.error("Reservation with id {} does not exist", reservationId);
                throw new ReservationDoesNotExistException(reservationId);
            }
        } else {
            LOGGER.error("User with id {} does not exist", userId);
            throw new UserDoesNotExistException(userId);
        }
    }

    public void cancelReservation(Long userId, UUID reservationId) {
        LOGGER.info("Cancelling user reservation, user id {}, reservation id {}", userId, reservationId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            LOGGER.info("Successfully retrieved user with id {}", userId);
            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
            if (optionalReservation.isPresent()) {
                LOGGER.info("Successfully retrieved reservation with id {}", reservationId);
                if (!optionalUser.get().equals(optionalReservation.get().getUser())) {
                    LOGGER.warn("User does not match with reservation user");
                    throw new UserDoesNotMatchReservationUserException();
                } else {
                    Reservation reservation = optionalReservation.get();
                    reservation.setCancelled(true);
                    reservationRepository.save(reservation);
                    LOGGER.info("Successfully cancelled reservation with id {}", reservation.getId());
                }
            } else {
                LOGGER.error("Reservation with id {} does not exist", reservationId);
                throw new ReservationDoesNotExistException(reservationId);
            }
        } else {
            LOGGER.error("User with id {} does not exist", userId);
            throw new UserDoesNotExistException(userId);
        }
    }


    //TODO delete reservations first
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
}
