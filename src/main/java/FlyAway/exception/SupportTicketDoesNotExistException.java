package FlyAway.exception;

public class SupportTicketDoesNotExistException extends RuntimeException {

    public SupportTicketDoesNotExistException() {
        super("Support ticket with given id does not exist");
    }

    public SupportTicketDoesNotExistException(String id) {
        super("Support ticket with id " + id + " does not exist");
    }

}
