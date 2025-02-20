package FlyAway.support;

import FlyAway.client.Client;
import FlyAway.client.ClientRepository;
import FlyAway.security.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientSupportTicketService {

    private final SupportTicketRepository ticketRepository;
    private final ChatMessageService chatMessageService;
    private final Logger LOGGER = LoggerFactory.getLogger(ClientSupportTicketService.class);

    public ClientSupportTicketService(SupportTicketRepository ticketRepository, ChatMessageService messageService, ClientRepository clientRepository) {
        this.ticketRepository = ticketRepository;
        this.chatMessageService = messageService;
    }

    public SupportTicket createTicket(CreateSupportTicketDto createSupportTicketDto, Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();

        SupportTicket supportTicket = SupportTicket.builder()
                .title(createSupportTicketDto.title())
                .clientId(client.getId())
                .status(TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        ticketRepository.save(supportTicket);
        LOGGER.info("Client {} created ticket {}",client.getEmail(), supportTicket);
        chatMessageService.addFirstMessageToTicket(createSupportTicketDto.message(), supportTicket);
        return supportTicket;
    }

    public List<SupportTicket> getOwnTickets(Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();
        List<SupportTicket> tickets = ticketRepository.findByClientId(client.getId());
        LOGGER.info("Client {} retrieved {} tickets", client.getEmail(), tickets.size());
        return tickets;
    }

    public List<ChatMessage> getChatMessages(String ticketId, Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();

        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("ticket does not exist")); //todo custom exception

        if (ticket.getClientId() != client.getId()) {
            throw new RuntimeException("Unaauthorize"); //todo
        }
        List<ChatMessage> chatMessages = chatMessageService.getChatMessages(ticketId);
        LOGGER.info("Retrieved {} messages", chatMessages.size());
        return chatMessages;
    }

    public SupportTicketSummaryDto getTicketSummary(String ticketId, Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();

        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("ticket does not exist")); //todo custom exception

        if (ticket.getClientId() != client.getId()) {
            throw new RuntimeException("Unaauthorize"); //todo
        }

        return new SupportTicketSummaryDto(ticket.getTitle(), ticket.getStatus());
    }
}
