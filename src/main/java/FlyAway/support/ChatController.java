package FlyAway.support;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/ticket/{ticketId}/send")
    @SendTo("/topic/ticket/{ticketId}")
    public ChatMessage sendMessage(@DestinationVariable String ticketId, @Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());

        // Zapisanie wiadomości do bazy
        chatMessageService.saveMessage(ticketId, chatMessage);

        // Wysłanie wiadomości do klientów subskrybujących dany ticket
        messagingTemplate.convertAndSend("/topic/ticket/" + ticketId, chatMessage);
        return chatMessage;
    }

}
