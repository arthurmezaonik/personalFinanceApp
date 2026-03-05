package br.com.personalFinanceApp.core.controller;

import br.com.personalFinanceApp.core.dto.ErrorResponseDto;
import br.com.personalFinanceApp.core.exceptions.HouseholdNotFoundException;
import br.com.personalFinanceApp.core.exceptions.InvalidNameUpdateException;
import br.com.personalFinanceApp.core.exceptions.NameAlreadyRegisteredException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(HouseholdNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(HouseholdNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({
        InvalidNameUpdateException.class,
        NameAlreadyRegisteredException.class,
        IllegalArgumentException.class,
        ValidationException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(RuntimeException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return buildError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format(
            "Invalid parameter '%s'. Expected type %s.",
            ex.getName(),
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );
        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleBeanValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected internal error occurred");
    }

    private ResponseEntity<ErrorResponseDto> buildError(HttpStatus status, String message) {
        return ResponseEntity.status(status)
            .body(new ErrorResponseDto(status.value(), status.getReasonPhrase(), message));
    }
}
