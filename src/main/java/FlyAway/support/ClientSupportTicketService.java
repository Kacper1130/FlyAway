package FlyAway.support;

import FlyAway.client.Client;
import FlyAway.client.ClientRepository;
import FlyAway.exception.SupportTicketDoesNotExistException;
import FlyAway.security.SecurityUser;
import FlyAway.support.chat.ChatMessage;
import FlyAway.support.chat.ChatMessageService;
import FlyAway.support.dto.CreateSupportTicketDto;
import FlyAway.support.dto.SupportTicketSummaryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientSupportTicketService {

    private final SupportTicketRepository ticketRepository;
    private final ChatMessageService chatMessageService;
    private final Logger LOGGER = LoggerFactory.getLogger(ClientSupportTicketService.class);

    public ClientSupportTicketService(SupportTicketRepository ticketRepository, ChatMessageService messageService) {
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
        LOGGER.info("Client {} created ticket {}", client.getEmail(), supportTicket);
        chatMessageService.addFirstMessageToTicket(createSupportTicketDto.message(), supportTicket);
        return supportTicket;
    }

    public List<SupportTicket> getOwnTickets(Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();
        List<SupportTicket> tickets = ticketRepository.findByClientIdOrderByCreatedAtDesc(client.getId());
        LOGGER.info("Client {} retrieved {} own tickets", client.getEmail(), tickets.size());
        return tickets;
    }

    public List<ChatMessage> getChatMessages(String ticketId, Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();

        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    LOGGER.error("Client {} attempted to access non-existent ticket ID {}", client.getEmail(), ticketId);
                    return new SupportTicketDoesNotExistException(ticketId);
                });

        if (!ticket.getClientId().equals(client.getId())) {
            LOGGER.error("Access denied: Client {} attempted to access ticket ID {} owned by another client", client.getEmail(), ticketId);
            throw new AccessDeniedException("Access denied: You are not the owner of this support ticket");
        }

        List<ChatMessage> chatMessages = chatMessageService.getChatMessages(ticketId);
        LOGGER.info("Client {} retrieved {} messages for ticket ID {}", client.getEmail(), chatMessages.size(), ticketId);
        return chatMessages;
    }


    public SupportTicketSummaryDto getTicketSummary(String ticketId, Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        Client client = (Client) securityUser.getUser();

        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    LOGGER.error("Client {} attempted to access non-existent ticket ID {}", client.getEmail(), ticketId);
                    return new SupportTicketDoesNotExistException(ticketId);
                });

        if (!ticket.getClientId().equals(client.getId())) {
            LOGGER.error("Access denied: Client {} attempted to access summary of ticket ID {}", client.getEmail(), ticketId);
            throw new AccessDeniedException("Access denied: You are not the owner of this support ticket");
        }

        LOGGER.info("Client {} successfully retrieved summary for ticket ID {}", client.getEmail(), ticketId);
        return new SupportTicketSummaryDto(ticket.getTitle(), ticket.getStatus());
    }
}
