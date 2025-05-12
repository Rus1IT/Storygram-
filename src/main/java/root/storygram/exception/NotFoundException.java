package root.storygram.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    private final String fieldName;

    public NotFoundException(String message, String fieldName){
        super(message);
        this.fieldName = fieldName;
    }

    public NotFoundException(String message, Throwable cause, String fieldName){
        super(message, cause);
        this.fieldName = fieldName;
    }
}
