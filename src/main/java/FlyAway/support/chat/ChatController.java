package FlyAway.support.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/ticket/{ticketId}/send")
    public ChatMessage sendMessage(@DestinationVariable String ticketId, @Payload ChatMessage chatMessage) {
        LOGGER.info("message: {}, ticketId: {}", chatMessage, ticketId);
        chatMessage.setTimestamp(LocalDateTime.now());

        chatMessageService.saveMessage(chatMessage);

        messagingTemplate.convertAndSend("/topic/ticket/" + ticketId, chatMessage);
        return chatMessage;
    }

}
