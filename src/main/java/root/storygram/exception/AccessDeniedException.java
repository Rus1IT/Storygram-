package root.storygram.exception;

import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException{
    private final String fieldName;

    public AccessDeniedException(String message, String fieldName){
        super(message);
        this.fieldName = fieldName;
    }
}
