package FlyAway.support.dto;

import FlyAway.support.TicketStatus;

public record SupportTicketSummaryDto(
        String title,
        TicketStatus status
) {
}
