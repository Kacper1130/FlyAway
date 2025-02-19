package FlyAway.support;

import FlyAway.client.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeSupportTicketService {

    private final SupportTicketRepository ticketRepository;
    private final ChatMessageService chatMessageService;
    private final ClientRepository clientRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(EmployeeSupportTicketService.class);

    public EmployeeSupportTicketService(SupportTicketRepository ticketRepository, ChatMessageService chatMessageService, ClientRepository clientRepository) {
        this.ticketRepository = ticketRepository;
        this.chatMessageService = chatMessageService;
        this.clientRepository = clientRepository;
    }

    public List<SupportTicket> getTickets() {
        return ticketRepository.findAll();
    }

}
