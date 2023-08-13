package muffintop.cym.api.exception;

public class ProjectUpdateFailException extends RuntimeException {

    public ProjectUpdateFailException() {
    }

    public ProjectUpdateFailException(String message) {
        super(message);
    }
}
