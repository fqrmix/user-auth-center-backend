package com.fqrmix.authcenterback.exception;

import com.fqrmix.authcenterback.dto.response.api.impl.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
public class CustomRestExceptionHandler {

    /**
     * Handles all uncaught exceptions.
     *
     * @param ex      The exception that occurred.
     * @param request The web request.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiErrorResponse apiError = ApiErrorResponse.builder()
                .withType("error")
                .withError(ex.getLocalizedMessage())
                .withMessage(ex.getClass().getSimpleName())
                .withCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Handles validation errors for method arguments.
     *
     * @param ex      The MethodArgumentNotValidException that occurred.
     * @param request The web request.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleValidationError(MethodArgumentNotValidException ex, WebRequest request) {
        List<ConstraintsViolationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        new ConstraintsViolationError(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                )
                .toList();

        ApiErrorResponse apiError = ApiErrorResponse.builder()
                .withType("error")
                .withCode(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .withMessage(ex.getClass().getSimpleName())
                .withErrors(validationErrors)
                .build();

        return new ResponseEntity<>(
                apiError,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }
}
