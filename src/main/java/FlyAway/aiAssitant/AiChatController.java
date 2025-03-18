package FlyAway.aiAssitant;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@Tag(name = "AiChat")
public class AiChatController {
    private final AiChatService chatService;

    public AiChatController(AiChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<AiChatMessage> useChat(@RequestBody AiChatMessage prompt) {
        AiChatMessage response = this.chatService.useChat(prompt);
        return ResponseEntity.ok(response);
    }
}
