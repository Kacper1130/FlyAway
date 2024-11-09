package FlyAway.exception;

public class CountryDoesNotExistException extends RuntimeException {

    public CountryDoesNotExistException() {
        super("Country with given id does not exist");
    }

    public CountryDoesNotExistException(String countryName) {
        super(String.format("Country %s does not exist", countryName));
    }

}
