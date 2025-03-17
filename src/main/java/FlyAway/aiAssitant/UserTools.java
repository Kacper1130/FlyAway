package FlyAway.aiAssitant;

import org.springframework.ai.tool.annotation.Tool;

public class UserTools {

    @Tool(description = "get the current user email")
    String getUserEmail() {
        return "Kacper12@gmail.com"; //todo
    }
}
