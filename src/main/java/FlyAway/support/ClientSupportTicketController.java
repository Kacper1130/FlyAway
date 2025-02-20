package FlyAway.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/tickets")
@Tag(name = "ClientSupportTicket")
public class ClientSupportTicketController {

    private final ClientSupportTicketService supportTicketService;
    Logger LOGGER = LoggerFactory.getLogger(ClientSupportTicketController.class);

    public ClientSupportTicketController(ClientSupportTicketService supportTicketService) {
        this.supportTicketService = supportTicketService;
    }

    @PostMapping()
    public ResponseEntity<SupportTicket> createTicket(@RequestBody CreateSupportTicketDto createSupportTicketDto, Authentication authentication) {
        LOGGER.info("Creating ticket {}", createSupportTicketDto);
        return ResponseEntity.ok(supportTicketService.createTicket(createSupportTicketDto, authentication));
    }

    @GetMapping()
    public ResponseEntity<List<SupportTicket>> getOwnTickets(Authentication authentication) {
        return ResponseEntity.ok(supportTicketService.getOwnTickets(authentication));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable String ticketId, Authentication authentication) {
        return ResponseEntity.ok(supportTicketService.getChatMessages(ticketId, authentication));
    }

    @GetMapping("/{ticketId}/summary")
    ResponseEntity<SupportTicketSummaryDto> getTicketSummary(@PathVariable String ticketId, Authentication authentication) {
        return ResponseEntity.ok(supportTicketService.getTicketSummary(ticketId, authentication));
    }

}
