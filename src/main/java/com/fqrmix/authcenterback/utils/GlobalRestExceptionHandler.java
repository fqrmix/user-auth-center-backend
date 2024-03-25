package com.fqrmix.authcenterback.utils;

import com.fqrmix.authcenterback.exception.UserAlreadyExistsException;
import com.fqrmix.authcenterback.models.ErrorObject;
import com.fqrmix.authcenterback.dto.response.api.ErrorResponse;
import com.fqrmix.authcenterback.dto.response.api.impl.ApiErrorResponseImpl;
import com.fqrmix.authcenterback.models.enums.ErrorsConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
public class GlobalRestExceptionHandler {

    /**
     * Handles bad credentials errors for method arguments.
     *
     * @param ex      The BadCredentialsException that occurred.
     * @param request The web request.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<ApiErrorResponseImpl> handleAuthenticationException(
            BadCredentialsException ex,
            WebRequest request
    ) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    ApiErrorResponseImpl.builder()
                            .withType("error")
                            .withCode(ex.getClass().getSimpleName())
                            .withMessage(ex.getLocalizedMessage())
                            .withErrors(List.of(ErrorObject.builder()
                                                .withError(ErrorsConstants.BAD_CREDENTIALS.getError())
                                                .withDescription(ErrorsConstants.BAD_CREDENTIALS.getDescription())
                                                .build()))
                            .build()
                );
    }

    /**
     * Handles access denied errors for method arguments.
     *
     * @param ex      The AccessDeniedException that occurred.
     * @param request The web request.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ApiErrorResponseImpl> handleAuthenticationException(
            AccessDeniedException ex,
            WebRequest request
    ) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    ApiErrorResponseImpl.builder()
                            .withType("error")
                            .withCode(ex.getClass().getSimpleName())
                            .withMessage(ex.getLocalizedMessage())
                            .withErrors(List.of(ErrorObject.builder()
                                    .withError(ErrorsConstants.ACCESS_DENIED.getError())
                                    .withDescription(ErrorsConstants.ACCESS_DENIED.getDescription())
                                    .build()))
                            .build()
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
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex,
            WebRequest request
    ) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    ApiErrorResponseImpl.builder()
                            .withType("error")
                            .withCode(ex.getClass().getSimpleName())
                            .withMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .withErrors(List.of(ErrorObject.builder()
                                    .withError(ErrorsConstants.USER_ALREADY_EXISTS.getError())
                                    .withDescription(ErrorsConstants.USER_ALREADY_EXISTS.getDescription())
                                    .build()))
                            .build()
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
    public ResponseEntity<ErrorResponse> handleValidationError(
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    ApiErrorResponseImpl.builder()
                            .withType("error")
                            .withCode(ex.getClass().getSimpleName())
                            .withMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .withErrors(validationErrors)
                            .build()
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
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, WebRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    ApiErrorResponseImpl.builder()
                            .withType("error")
                            .withMessage(ex.getLocalizedMessage())
                            .withCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                            .withErrors(List.of(ErrorObject.builder()
                                    .withError(ErrorsConstants.INTERNAL_SERVER_ERROR.getError())
                                    .withDescription(ErrorsConstants.INTERNAL_SERVER_ERROR.getDescription())
                                    .build()))
                            .build()
                );
    }
}
