package FlyAway.support;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Setter
@ToString
@Builder
public class SupportTicket {

    @Id
    private String id;
    private String title;
    private Long clientId;
    private Long employeeId;
    private TicketStatus status;
    private LocalDateTime createdAt;

}
