package muffintop.cym.api.exception;

public class ExistingEmailCodeException extends RuntimeException {

    public ExistingEmailCodeException() {
    }

    public ExistingEmailCodeException(String message) {
        super(message);
    }
}
