package FlyAway.support.chat;

import FlyAway.support.SupportTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository messageRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(ChatMessageService.class);

    public ChatMessageService(ChatMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void addFirstMessageToTicket(String content, SupportTicket supportTicket) {
        ChatMessage message = ChatMessage.builder()
                .ticketId(supportTicket.getId())
                .senderId(supportTicket.getClientId())
                .senderType(SenderType.CLIENT)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);
        LOGGER.info("Created message {}", message);
    }

    public List<ChatMessage> getChatMessages(String ticketId) {
        return messageRepository.findByTicketId(ticketId);
    }

    public void saveMessage(ChatMessage chatMessage) {
        messageRepository.save(chatMessage);
        LOGGER.info("saved message {}", chatMessage);
    }
}
