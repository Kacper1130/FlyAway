package FlyAway.exception;

public class TotalSeatsMismatchException extends RuntimeException {

    public TotalSeatsMismatchException() {
        super("Total seats in seat classes does not match total seats on the aircraft");
    }

    public TotalSeatsMismatchException(Integer totalSeatsInClasses, Integer totalSeatsOnAircraft) {
        super(String.format("Total seats in seat classes (%d) does not match total seats on the aircraft (%d)",
                totalSeatsInClasses, totalSeatsOnAircraft));
    }
}
