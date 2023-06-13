package muffintop.cym.api.exception;

public class DuplicatedIdException extends RuntimeException{

    public DuplicatedIdException(){}

    public DuplicatedIdException(String message){
        super(message);
    }
}
