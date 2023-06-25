package muffintop.cym.api.exception;

public class NonExistIdException extends RuntimeException{

    public NonExistIdException(){}

    public NonExistIdException(String message){
        super(message);
    }
}
