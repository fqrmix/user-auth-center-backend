package com.fqrmix.authcenterback.exception;

import com.fqrmix.authcenterback.dto.response.api.ErrorObject;
import com.fqrmix.authcenterback.dto.response.api.IErrorResponse;
import com.fqrmix.authcenterback.dto.response.api.impl.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
     * Handles bad credentials errors for method arguments.
     *
     * @param ex      The BadCredentialsException that occurred.
     * @param request The web request.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            BadCredentialsException ex,
            WebRequest request
    ) {

        return new ResponseEntity<>(
                ApiErrorResponse.builder()
                        .withType("error")
                        .withCode(ex.getClass().getSimpleName())
                        .withMessage(ex.getLocalizedMessage())
                        .withErrors(List.of(ErrorObject.builder()
                                                .withError(Errors.BAD_CREDENTIALS.getError())
                                                .withDescription(Errors.BAD_CREDENTIALS.getDescription())
                                                .build()))
                        .build(),
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED
        );
    }

    /**
     * Handles validation errors for method arguments.
     *
     * @param ex      The UserAlreadyExistsException that occurred.
     * @param request The web request.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({ UserAlreadyExistsException.class })
    public ResponseEntity<IErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex,
            WebRequest request
    ) {

        return new ResponseEntity<IErrorResponse>(
                ApiErrorResponse.builder()
                        .withType("error")
                        .withCode(ex.getClass().getSimpleName())
                        .withMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .withErrors(List.of(
                                ErrorObject.builder()
                                        .withError(Errors.USER_ALREADY_EXISTS.getError())
                                        .withDescription(Errors.USER_ALREADY_EXISTS.getDescription())
                                        .build()
                        ))
                        .build(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
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
    public ResponseEntity<IErrorResponse> handleValidationError(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        List<ErrorObject> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        ErrorObject.builder()
                                .withError(error.getField())
                                .withDescription(error.getDefaultMessage())
                                .build()
                ).toList();

        return new ResponseEntity<IErrorResponse>(
                ApiErrorResponse.builder()
                        .withType("error")
                        .withCode(ex.getClass().getSimpleName())
                        .withMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .withErrors(validationErrors)
                        .build(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handles all uncaught exceptions.
     *
     * @param ex      The exception that occurred.
     * @param request The web request.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<IErrorResponse> handleAll(Exception ex, WebRequest request) {

        return new ResponseEntity<IErrorResponse>(
                ApiErrorResponse.builder()
                        .withType("error")
                        .withErrors(List.of())
                        .withMessage(ex.getClass().getSimpleName())
                        .withCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .withErrors(List.of(ErrorObject.builder()
                                .withError(Errors.INTERNAL_SERVER_ERROR.getError())
                                .withDescription(Errors.INTERNAL_SERVER_ERROR.getDescription())
                                .build()))
                        .build(),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
