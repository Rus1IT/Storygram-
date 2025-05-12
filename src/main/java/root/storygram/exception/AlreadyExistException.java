package root.storygram.exception;

import lombok.Getter;

@Getter
public class AlreadyExistException extends RuntimeException{
    private final String fieldName;

    public AlreadyExistException(String fieldName){
        super(String.format("%s already exists", fieldName));
        this.fieldName = fieldName;
    }

    public AlreadyExistException(String message,String fieldName){
        super(message);
        this.fieldName = fieldName;
    }

    public AlreadyExistException(String message, Throwable cause, String fieldName){
        super(message, cause);
        this.fieldName = fieldName;
    }
}
