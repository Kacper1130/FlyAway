package FlyAway.exception;

public class CountryDoesNotExistException extends RuntimeException {

    public CountryDoesNotExistException() {
        super("Country with given id does not exist");
    }

}
