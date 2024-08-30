package FlyAway.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class ErrorMessage {
    private int statusCode;
    private String message;
    private Set<String> errors;

    public ErrorMessage(int statusCode, String message, Set<String> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }

}
