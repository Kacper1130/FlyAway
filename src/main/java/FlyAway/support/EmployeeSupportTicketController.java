package FlyAway.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee/tickets")
@Tag(name = "EmployeeSupportTicket")
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public class EmployeeSupportTicketController {

    private final EmployeeSupportTicketService supportTicketService;
    private final Logger LOGGER = LoggerFactory.getLogger(EmployeeSupportTicketController.class);

    public EmployeeSupportTicketController(SupportTicketRepository ticketRepository, EmployeeSupportTicketService supportTicketService) {
        this.supportTicketService = supportTicketService;
    }

    @GetMapping()
    public ResponseEntity<List<SupportTicket>> getTickets() {
        return ResponseEntity.ok(supportTicketService.getTickets());
    }

}
