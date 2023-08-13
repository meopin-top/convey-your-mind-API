package muffintop.cym.api.exception;

public class ProjectCreateFailException extends RuntimeException {

    public ProjectCreateFailException() {
    }

    public ProjectCreateFailException(String message) {
        super(message);
    }
}
