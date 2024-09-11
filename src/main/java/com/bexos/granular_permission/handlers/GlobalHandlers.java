package com.bexos.granular_permission.handlers;

import com.bexos.granular_permission.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class GlobalHandlers {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequests(BadRequestException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundRequests(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnAuthorizedRequests(UnAuthorizedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleException(AccessDeniedException exp) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .status(FORBIDDEN.value())
                        .message("Access denied")
                        .build());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();

        String errorMessage;
        if (!globalErrors.isEmpty()) {
            errorMessage = globalErrors.getFirst().getDefaultMessage();
        } else if (!fieldErrors.isEmpty()) {
            errorMessage = fieldErrors.getFirst().getDefaultMessage();
        } else {
            errorMessage = "Validation failed";
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(errorMessage)
                        .build());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ExceptionResponse.builder().message(exp.getMessage()).build());
//    }
}
