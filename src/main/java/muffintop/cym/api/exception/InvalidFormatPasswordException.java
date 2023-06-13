package muffintop.cym.api.exception;

public class InvalidFormatPasswordException extends RuntimeException{

    public InvalidFormatPasswordException(){}

    public InvalidFormatPasswordException(String message){
        super(message);
    }
}
