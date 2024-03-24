package com.fqrmix.authcenterback.interceptors;

import java.io.IOException;

import com.fqrmix.authcenterback.dto.response.api.impl.ApiErrorResponseImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Component class serving as the entry point for JWT authentication errors.
 */
@Component
@Slf4j
public class AuthEntryPointInterceptor implements AuthenticationEntryPoint {

    /**
     * Handles unauthorized access errors by returning an appropriate response.
     *
     * @param request       The HTTP request.
     * @param response      The HTTP response.
     * @param authException The authentication exception that occurred.
     * @throws IOException      If an I/O error occurs during response writing.
     * @throws ServletException If the request cannot be handled properly.
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        log.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiErrorResponseImpl apiResponse = ApiErrorResponseImpl.builder()
                .withType("error")
                .withCode(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .withMessage(authException.getMessage())
                .build();

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), apiResponse);
    }

}
