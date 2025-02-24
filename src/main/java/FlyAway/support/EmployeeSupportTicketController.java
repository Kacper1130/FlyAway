package FlyAway.support;

import FlyAway.support.chat.ChatMessage;
import FlyAway.support.dto.SupportTicketSummaryDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee/tickets")
@Tag(name = "EmployeeSupportTicket")
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public class EmployeeSupportTicketController {

    private final EmployeeSupportTicketService supportTicketService;
    private final Logger LOGGER = LoggerFactory.getLogger(EmployeeSupportTicketController.class);

    public EmployeeSupportTicketController(EmployeeSupportTicketService supportTicketService) {
        this.supportTicketService = supportTicketService;
    }

    @GetMapping()
    public ResponseEntity<List<SupportTicket>> getTickets(Authentication authentication) {
        return ResponseEntity.ok(supportTicketService.getTickets(authentication));
    }

    @GetMapping("/{ticketId}/messages")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable String ticketId) {
        return ResponseEntity.ok(supportTicketService.getChatMessages(ticketId));
    }

    @GetMapping("/{ticketId}/summary")
    ResponseEntity<SupportTicketSummaryDto> getTicketSummary(@PathVariable String ticketId) {
        return ResponseEntity.ok(supportTicketService.getTicketSummary(ticketId));
    }

    @PatchMapping("/{ticketId}/close")
    public ResponseEntity<Void> closeTicket(@PathVariable String ticketId) {
        supportTicketService.closeTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{ticketId}/assign")
    public ResponseEntity<Void> assignTicket(@PathVariable String ticketId, Authentication authentication) {
        supportTicketService.assignTicket(ticketId, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active-tickets-count")
    public ResponseEntity<Integer> getActiveTicketsCount(Authentication authentication) {
        return ResponseEntity.ok(supportTicketService.getActiveTicketsCount(authentication));
    }
}
