package muffintop.cym.api.exception;

public class ExistingInviteCodeException extends RuntimeException {

    public ExistingInviteCodeException() {
    }

    public ExistingInviteCodeException(String message) {
        super(message);
    }
}
