package FlyAway.aiAssitant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiChatService {

    private final ChatClient chatClient;

    public AiChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public AiChatMessage useChat(AiChatMessage prompt) {
        var response = chatClient.prompt()
                .user(prompt.message())
                .call();

        return new AiChatMessage(response.content());
    }

}
