package FlyAway.exception;

import FlyAway.flight.aircraft.CabinClass;
import jakarta.validation.constraints.NotNull;

public class CabinClassDoesNotExistException extends RuntimeException {

    public CabinClassDoesNotExistException(@NotNull CabinClass cabinClass) {
        super(String.format("Cabin class %s does not exist", cabinClass));
    }
}
