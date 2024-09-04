package FlyAway.exception;

public class AccountNotActivatedException extends RuntimeException {

    public AccountNotActivatedException() {
        super("Account is not verified, please check your email");
    }


}
