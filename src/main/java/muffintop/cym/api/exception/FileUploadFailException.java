package muffintop.cym.api.exception;

public class FileUploadFailException extends RuntimeException {

    public FileUploadFailException() {
    }

    public FileUploadFailException(String message) {
        super(message);
    }
}
