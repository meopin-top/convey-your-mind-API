package muffintop.cym.api.exception;

public class IncorrectPasswordException extends RuntimeException{

    public IncorrectPasswordException(){}

    public IncorrectPasswordException(String message){
        super(message);
    }
}
