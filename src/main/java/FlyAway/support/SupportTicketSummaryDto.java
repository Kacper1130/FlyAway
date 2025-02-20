package FlyAway.support;

public record SupportTicketSummaryDto(
        String title,
        TicketStatus status
) {
}
