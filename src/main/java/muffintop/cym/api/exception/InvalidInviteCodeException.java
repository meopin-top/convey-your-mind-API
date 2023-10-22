package muffintop.cym.api.exception;

public class InvalidInviteCodeException extends RuntimeException {

    public InvalidInviteCodeException() {
    }

    public InvalidInviteCodeException(String message) {
        super(message);
    }
}
