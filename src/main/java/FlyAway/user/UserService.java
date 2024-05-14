package FlyAway.user;

import FlyAway.exception.EmailExistsException;
import FlyAway.exception.ReservationDoesNotExistException;
import FlyAway.exception.UserDoesNotExistException;
import FlyAway.exception.UserDoesNotMatchReservationUserException;
import FlyAway.reservation.Reservation;
import FlyAway.reservation.ReservationMapper;
import FlyAway.reservation.ReservationRepository;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.security.RoleRepository;
import FlyAway.user.dto.UserDto;
import FlyAway.user.dto.UserRegistrationDto;
import FlyAway.user.dto.UserReservationDto;
import org.mapstruct.factory.Mappers;
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
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, RoleRepository roleRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<UserDto> getAll() {
        LOGGER.debug("Retrieving all users from repository");
        List<UserDto> users = userRepository.findAll()
                .stream().map(userMapper::userToUserDto)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} users from repository", users.size());
        return users;
    }

    public List<UserDto> getAllActiveUsers() {
        LOGGER.debug("Retrieving all active users from repository");
        List<UserDto> users = userRepository.findAllActiveUsers()
                .stream().map(userMapper::userToUserDto)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} active users from repository", users.size());
        return users;
    }

    public List<UserReservationDto> getAllDeletedUsers() {
        LOGGER.debug("Retrieving deleted users from repository");
        List<UserReservationDto> users = userRepository.findAllDeletedUsers()
                .stream().map(userMapper::userToUserReservationDto)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} deleted users from repository", users.size());
        return users;
    }

    public UserDto addUser(UserRegistrationDto userRegistrationDto) {
        LOGGER.debug("Adding new user");

        if (userRepository.findUserByEmail(userRegistrationDto.email()) != null) {
            LOGGER.error("User with email {} already exists", userRegistrationDto.email());
            throw new EmailExistsException(userRegistrationDto.email());
        }

        User mappedUser = userMapper.userRegistrationDtoToUser(userRegistrationDto);
        var role = roleRepository.findByName("ROLE_USER");
        mappedUser.setRoles(Set.of(role));
        userRepository.save(mappedUser);
        LOGGER.info("Created new user with id {}", mappedUser.getId());

        UserDto createdUserDto = userMapper.userToUserDto(mappedUser);
        return createdUserDto;
    }

    public UserDto getUser(Long id) {
        LOGGER.debug("Retrieving user with id {}", id);
        Optional<User> optionalUser = userRepository.findActiveById(id);
        return optionalUser.map(
                u -> {
                    LOGGER.info("Successfully retrieved user with id {}", id);
                    return userMapper.userToUserDto(u);
                }
        ).orElseThrow(() -> {
            LOGGER.error("User with id {} does not exist", id);
            throw new UserDoesNotExistException(id);
        });
    }

    public UserReservationDto getUserWithReservations(Long id) {
        LOGGER.debug("Retrieving user with reservations, user id {} ", id);
        Optional<User> optionalUser = userRepository.findActiveById(id);

        return optionalUser.map(
                u -> {
                    LOGGER.info("Successfully retrieved user with reservations, user id {}", id);
                    return userMapper.userToUserReservationDto(u);
                }
        ).orElseThrow(() -> {
            LOGGER.error("User with id {} does not exist", id);
            throw new UserDoesNotExistException(id);
        });
    }

    public ReservationDto getUserReservation(Long userId, UUID reservationId) {
        LOGGER.debug("Retrieving user reservation, user id {}, reservation id {}", userId, reservationId);
        Optional<User> optionalUser = userRepository.findActiveById(userId);
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
                    return reservationMapper.reservationToReservationDto(r);
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
        LOGGER.debug("Cancelling user reservation, user id {}, reservation id {}", userId, reservationId);
        Optional<User> optionalUser = userRepository.findActiveById(userId);
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

    public void deleteUser(Long id) {
        LOGGER.debug("Deleting user with id {}", id);

        Optional<User> optionalUser = userRepository.findActiveById(id);
        if (optionalUser.isEmpty()) {
            throw new UserDoesNotExistException(id);
        }

        User user = optionalUser.get();

        if (!user.getReservations().isEmpty()) {
            LOGGER.warn("User have active reservation(s)");
            List<Reservation> reservations = user.getReservations();
            reservations.stream().forEach(
                    reservation -> {
                        reservation.setCancelled(true);
                        reservationRepository.save(reservation);
                        LOGGER.info("Successfully cancelled reservation with id {}",reservation.getId());
                    }
            );
        }

        user.setDeleted(true);
        userRepository.save(user);
        LOGGER.info("Successfully deleted user with id {}", id);

    }

}
