package muffintop.cym.api.exception;

public class NoEmailException extends RuntimeException {

    public NoEmailException() {
    }

    public NoEmailException(String message) {
        super(message);
    }
}
