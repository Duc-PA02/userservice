package org.klb.userservice.exception;

import lombok.RequiredArgsConstructor;
import org.klb.userservice.controller.dto.ApiResponseWrapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalException {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Locale locale = request.getLocale();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String localizedErrorMessage = messageSource.getMessage(Objects.requireNonNull(error.getDefaultMessage()), null, locale);
            errors.put(error.getField(), localizedErrorMessage);
        });

        return new ResponseEntity<>(new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                errors.toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<?> handleDuplicateFieldException(DuplicateFieldException ex) {
        return new ResponseEntity<>(new ApiResponseWrapper<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponseWrapper<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        ), HttpStatus.NOT_FOUND);
    }
}
