package FlyAway.aiAssitant;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiChatService {

    private final ChatClient chatClient;

    public AiChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public AiChatMessage useChat(AiChatMessage prompt) {
        return chatClient.prompt()
                .user(prompt.message())
                .call()
                .entity(AiChatMessage.class);
    }

}
