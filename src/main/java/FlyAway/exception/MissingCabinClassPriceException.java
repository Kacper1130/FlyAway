package FlyAway.exception;

public class MissingCabinClassPriceException extends RuntimeException {

    public MissingCabinClassPriceException() {
        super("Missing price for cabin class");
    }
    public MissingCabinClassPriceException(String cabinClass) {
        super(String.format("Missing price for cabin class: %s", cabinClass));
    }
}
