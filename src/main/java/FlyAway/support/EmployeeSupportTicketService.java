package FlyAway.support;

import FlyAway.client.Client;
import FlyAway.employee.Employee;
import FlyAway.security.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeSupportTicketService {

    private final SupportTicketRepository ticketRepository;
    private final ChatMessageService chatMessageService;
    private final Logger LOGGER = LoggerFactory.getLogger(EmployeeSupportTicketService.class);

    public EmployeeSupportTicketService(SupportTicketRepository ticketRepository, ChatMessageService chatMessageService) {
        this.ticketRepository = ticketRepository;
        this.chatMessageService = chatMessageService;
    }

    public List<SupportTicket> getTickets() {
        return ticketRepository.findAll();
    }

    public List<ChatMessage> getChatMessages(String ticketId) {
        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("ticket does not exist")); //todo custom exception

        List<ChatMessage> chatMessages = chatMessageService.getChatMessages(ticketId);
        LOGGER.info("Retrieved {} messages", chatMessages.size());
        return chatMessages;
    }

    public SupportTicketSummaryDto getTicketSummary(String ticketId) {
        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("ticket does not exist")); //todo custom exception

        return new SupportTicketSummaryDto(ticket.getTitle(), ticket.getStatus());
    }

    public void closeTicket(String ticketId) {
        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("ticket does not exist")); //todo custom exception

        if (ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            throw new RuntimeException("Can not close ticket");
        }
        ticket.setStatus(TicketStatus.CLOSED);
        ticketRepository.save(ticket);
        LOGGER.info("Closed ticket with id {}", ticketId);
    }

    public void assignTicket(String ticketId, Authentication authentication) {
        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("ticket does not exist")); //todo custom exception
        LOGGER.info("ticket before assigning     {}", ticket);
        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new RuntimeException("Can not assign ticket");
        }

        var securityUser = (SecurityUser) authentication.getPrincipal();
        Employee employee = (Employee) securityUser.getUser();

        ticket.setEmployeeId(employee.getId());
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticketRepository.save(ticket);
        LOGGER.info("saved ticket {}", ticket);
    }
}
