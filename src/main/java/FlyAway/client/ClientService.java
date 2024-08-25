package FlyAway.client;

import FlyAway.client.dto.ClientDto;
import FlyAway.client.dto.ClientRegistrationDto;
import FlyAway.client.dto.ClientReservationDto;
import FlyAway.exception.EmailExistsException;
import FlyAway.exception.ReservationDoesNotExistException;
import FlyAway.exception.UserDoesNotExistException;
import FlyAway.exception.UserDoesNotMatchReservationUserException;
import FlyAway.reservation.Reservation;
import FlyAway.reservation.ReservationMapper;
import FlyAway.reservation.ReservationRepository;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.role.RoleRepository;
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
public class ClientService {

    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final ReservationRepository reservationRepository;
    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);
    private final ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    public ClientService(ClientRepository userRepository, RoleRepository roleRepository, ReservationRepository reservationRepository) {
        this.clientRepository = userRepository;
        this.roleRepository = roleRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ClientDto> getAll() {
        LOGGER.debug("Retrieving all clients from repository");
        List<ClientDto> clients = clientRepository.findAll()
                .stream().map(clientMapper::clientToClientDto)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} clients from repository", clients.size());
        return clients;
    }

    public List<ClientDto> getAllActiveClients() {
        LOGGER.debug("Retrieving all active clients from repository");
        List<ClientDto> clients = clientRepository.findAllActiveClients()
                .stream().map(clientMapper::clientToClientDto)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} active clients from repository", clients.size());
        return clients;
    }

    public List<ClientReservationDto> getAllDeletedClients() {
        LOGGER.debug("Retrieving deleted users from repository");
        List<ClientReservationDto> users = clientRepository.findAllDeletedClients()
                .stream().map(clientMapper::clientToClientReservationDto)
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} deleted users from repository", users.size());
        return users;
    }

    public ClientDto addClient
            (ClientRegistrationDto userRegistrationDto) {
        LOGGER.debug("Adding new user");

        if (clientRepository.findByEmail(userRegistrationDto.email()).isPresent()) {
            LOGGER.error("User with email {} already exists", userRegistrationDto.email());
            throw new EmailExistsException(userRegistrationDto.email());
        }

        Client mappedClient = clientMapper.clientRegistrationDtoToClient(userRegistrationDto);
        var role = roleRepository.findByName("ROLE_USER").orElseThrow();
        mappedClient.setRoles(Set.of(role));
        clientRepository.save(mappedClient);
        LOGGER.info("Created new user with id {}", mappedClient.getId());

        ClientDto createdUserDto = clientMapper.clientToClientDto(mappedClient);
        return createdUserDto;
    }

    public ClientDto getClient
            (Long id) {
        LOGGER.debug("Retrieving user with id {}", id);
        Optional<Client> optionalUser = clientRepository.findActiveById(id);
        return optionalUser.map(
                u -> {
                    LOGGER.info("Successfully retrieved user with id {}", id);
                    return clientMapper.clientToClientDto(u);
                }
        ).orElseThrow(() -> {
            LOGGER.error("User with id {} does not exist", id);
            throw new UserDoesNotExistException(id);
        });
    }

    public ClientReservationDto getClientWithReservations(Long id) {
        LOGGER.debug("Retrieving user with reservations, user id {} ", id);
        Optional<Client> optionalUser = clientRepository.findActiveById(id);

        return optionalUser.map(
                u -> {
                    LOGGER.info("Successfully retrieved user with reservations, user id {}", id);
                    return clientMapper.clientToClientReservationDto(u);
                }
        ).orElseThrow(() -> {
            LOGGER.error("User with id {} does not exist", id);
            throw new UserDoesNotExistException(id);
        });
    }

    public ReservationDto getClientReservation(Long userId, UUID reservationId) {
        LOGGER.debug("Retrieving user reservation, user id {}, reservation id {}", userId, reservationId);
        Optional<Client> optionalUser = clientRepository.findActiveById(userId);
        if (optionalUser.isPresent()) {
            LOGGER.info("Successfully retrieved user with id {}", userId);
            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
            if (optionalReservation.isPresent()) {
                LOGGER.info("Successfully retrieved reservation with id {}", reservationId);
                if (!optionalUser.get().equals(optionalReservation.get().getClient())) {
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
        Optional<Client> optionalUser = clientRepository.findActiveById(userId);
        if (optionalUser.isPresent()) {
            LOGGER.info("Successfully retrieved user with id {}", userId);
            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
            if (optionalReservation.isPresent()) {
                LOGGER.info("Successfully retrieved reservation with id {}", reservationId);
                if (!optionalUser.get().equals(optionalReservation.get().getClient())) {
                    LOGGER.warn("Client does not match with reservation client");
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
            LOGGER.error("Client with id {} does not exist", userId);
            throw new UserDoesNotExistException(userId);
        }
    }

    public void deleteClient
            (Long id) {
        LOGGER.debug("Deleting user with id {}", id);

        Optional<Client> optionalClient = clientRepository.findActiveById(id);
        if (optionalClient.isEmpty()) {
            throw new UserDoesNotExistException(id);
        }

        Client client = optionalClient.get();

        if (!client.getReservations().isEmpty()) {
            LOGGER.warn("Client has active reservation(s)");
            List<Reservation> reservations = client.getReservations();
            reservations.stream().forEach(
                    reservation -> {
                        reservation.setCancelled(true);
                        reservationRepository.save(reservation);
                        LOGGER.info("Successfully cancelled reservation with id {}",reservation.getId());
                    }
            );
        }

        client.setDeleted(true);
        clientRepository.save(client);
        LOGGER.info("Successfully deleted client with id {}", id);

    }

}
