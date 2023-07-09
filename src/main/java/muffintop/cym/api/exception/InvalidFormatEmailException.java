package muffintop.cym.api.exception;

public class InvalidFormatEmailException extends RuntimeException {

    public InvalidFormatEmailException() {
    }

    public InvalidFormatEmailException(String message) {
        super(message);
    }
}
