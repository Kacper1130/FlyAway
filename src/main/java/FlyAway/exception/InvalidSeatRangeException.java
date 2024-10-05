package FlyAway.exception;

import FlyAway.flight.aircraft.CabinClass;

public class InvalidSeatRangeException extends RuntimeException {

    public InvalidSeatRangeException() {
        super("Invalid seat range");
    }
    public InvalidSeatRangeException(CabinClass cabinClass) {
        super(String.format("Start seat is greater than end seat in %s class", cabinClass.toString().toLowerCase()));
    }
}
