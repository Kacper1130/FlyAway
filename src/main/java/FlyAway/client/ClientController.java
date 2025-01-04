package FlyAway.client;

import FlyAway.client.dto.ClientDto;
import FlyAway.client.dto.ClientReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.client.dto.ClientRegistrationDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Client")
public class ClientController {

    private final ClientService clientService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAll() {
        LOGGER.debug("Retrieving all active clients");
        List<ClientDto> clients = clientService.getAllActiveClients();
        LOGGER.info("Retrieved {} active clients", clients.size());
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<ClientReservationDto>> getAllDeletedUsers() {
        LOGGER.debug("Retrieving deleted client");
        List<ClientReservationDto> clients = clientService.getAllDeletedClients();
        LOGGER.info("Retrieved {} deleted clients", clients.size());
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody ClientRegistrationDto userRegistrationDto) {
        LOGGER.debug("Adding new client " + userRegistrationDto);
        ClientDto clientDto = clientService.addClient(userRegistrationDto);
        LOGGER.info("Added new client " + clientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientFromId(@PathVariable Long id) {
        LOGGER.debug("Retrieving client with id {}", id);
        ClientDto clientDto = clientService.getClientFromId(id);
        LOGGER.info("Successfully retrieved client");
        return ResponseEntity.ok(clientDto);
    }

    @GetMapping("/client")
    public ResponseEntity<ClientDto> getClient(Authentication authentication) {
        LOGGER.debug("Retrieving client {}", authentication.getCredentials());
        ClientDto clientDto = clientService.getClient(authentication);
        LOGGER.info("Successfully retrieved client");
        return ResponseEntity.ok(clientDto);
    }

    @PatchMapping("/client")
    public ResponseEntity<ClientDto> updateClient(@Valid @RequestBody ClientDto clientDto, Authentication authentication) {
        LOGGER.debug("Retrieving client {}", authentication.getCredentials());
        ClientDto updatedClient = clientService.updateClient(clientDto, authentication);
        LOGGER.info("{} updated info.\nBefore {}, after {}",clientDto.email(), clientDto, updatedClient);
        return ResponseEntity.ok(updatedClient);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<ClientReservationDto> getClientWithReservations(@PathVariable Long id) {
        LOGGER.debug("Retrieving client's reservation, user id " + id);
        ClientReservationDto clientReservationDto = clientService.getClientWithReservations(id);
        LOGGER.info("Successfully retrieved client with reservations");
        return ResponseEntity.ok(clientReservationDto);

    }

    @GetMapping("/{id}/reservations/{reservationId}")
    public ResponseEntity<ReservationDto> getClientReservation(@PathVariable("id") Long userId, @PathVariable UUID reservationId) {
        LOGGER.debug("Retrieving client reservation, client id {}, reservation id {}", userId, reservationId);
        ReservationDto reservationDto = clientService.getClientReservation(userId, reservationId);
        return ResponseEntity.ok(reservationDto);

    }

    @DeleteMapping("/{userId}/reservations/{reservationId}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long userId, @PathVariable UUID reservationId) {
        LOGGER.debug("Cancelling reservation, client id {}, reservation id {}", userId, reservationId);
        clientService.cancelReservation(userId, reservationId);
        LOGGER.info("Successfully cancelled reservation");
        return ResponseEntity.ok("Cancelled reservation");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        LOGGER.debug("Deleting client with id {}", id);
        clientService.deleteClient(id);
        LOGGER.info("Successfully deleted client with id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
