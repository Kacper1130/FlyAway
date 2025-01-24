package FlyAway.exception;

public class SeatNumberDoesNotBelongToAnyCabinClassException extends RuntimeException {
    public SeatNumberDoesNotBelongToAnyCabinClassException(Integer seatNumber) {
        super("Seat number " + seatNumber + " does not belong to any CabinClass.");
    }
}
