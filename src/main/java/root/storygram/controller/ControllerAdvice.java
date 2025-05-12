package root.storygram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import root.storygram.dto.response.ApiResponse;
import root.storygram.exception.AccessDeniedException;
import root.storygram.exception.AlreadyExistException;
import root.storygram.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return buildErrorResponse(e.getMessage(), e.getFieldName(), NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<?> handleAlreadyExistException(AlreadyExistException e) {
        return buildErrorResponse(e.getMessage(), e.getFieldName(), CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        return buildErrorResponse(e.getMessage(),e.getFieldName(), FORBIDDEN);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleException(Exception e) {
//        return buildErrorResponse(e.getMessage(), "error", INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ApiResponse.ErrorDetail> errorDetails = e.getBindingResult().getFieldErrors().stream().map(fieldError -> ApiResponse.ErrorDetail.builder().field(fieldError.getField()).message(fieldError.getDefaultMessage()).build()).collect(Collectors.toList());

        return ResponseEntity.status(BAD_REQUEST).body(ApiResponse.error("Validation error", errorDetails));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildErrorResponse(e.getMessage(), "error", BAD_REQUEST);
    }

    public ResponseEntity<?> buildErrorResponse(String message, String field, HttpStatus status) {
        ApiResponse.ErrorDetail errorDetail = ApiResponse.ErrorDetail.builder().field(field).message(message).build();

        return ResponseEntity.status(status).body(ApiResponse.error(message, Collections.singletonList(errorDetail)));
    }

}
