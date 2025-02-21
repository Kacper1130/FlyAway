package FlyAway.support;

import FlyAway.employee.Employee;
import FlyAway.security.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeSupportTicketService {

    private final SupportTicketRepository ticketRepository;
    private final ChatMessageService chatMessageService;
    private final Logger LOGGER = LoggerFactory.getLogger(EmployeeSupportTicketService.class);

    public EmployeeSupportTicketService(SupportTicketRepository ticketRepository, ChatMessageService chatMessageService) {
        this.ticketRepository = ticketRepository;
        this.chatMessageService = chatMessageService;
    }

    public List<SupportTicket> getTickets(Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Long employeeId = securityUser.getUser().getId();
        return ticketRepository.findAll().stream()
                .sorted((t1, t2) -> {
                    int priority1 = getPriority(t1, employeeId);
                    int priority2 = getPriority(t2, employeeId);

                    if (priority1 != priority2) {
                        return Integer.compare(priority1, priority2);
                    }

                    if (t1.getStatus() == TicketStatus.IN_PROGRESS) {
                        return t1.getCreatedAt().compareTo(t2.getCreatedAt());
                    } else if (t1.getStatus() == TicketStatus.OPEN) {
                        return t1.getCreatedAt().compareTo(t2.getCreatedAt());
                    } else if (t1.getStatus() == TicketStatus.CLOSED) {
                        return t2.getCreatedAt().compareTo(t1.getCreatedAt());
                    }

                    throw new RuntimeException("Comparing error");
                })
                .collect(Collectors.toList());
    }

    private int getPriority(SupportTicket ticket, Long employeeId) {
        if (ticket.getStatus() == TicketStatus.IN_PROGRESS && ticket.getEmployeeId().equals(employeeId)) {
            return 1;
        } else if (ticket.getStatus() == TicketStatus.OPEN) {
            return 2;
        } else if (ticket.getStatus() == TicketStatus.CLOSED && ticket.getEmployeeId().equals(employeeId)) {
            return 3;
        }
        return 0;
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
