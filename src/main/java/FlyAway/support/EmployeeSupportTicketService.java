package FlyAway.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new RuntimeException("Ticket is already closed");
        }
        ticket.setStatus(TicketStatus.CLOSED);
        ticketRepository.save(ticket);
        LOGGER.info("Closed ticket with id {}", ticketId);
    }
}
