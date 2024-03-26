package com.fqrmix.authcenterback.controllers;

import com.fqrmix.authcenterback.dto.request.LoginRequestDTO;
import com.fqrmix.authcenterback.dto.request.RegisterRequestDTO;
import com.fqrmix.authcenterback.dto.response.api.impl.ApiSuccessResponseImpl;
import com.fqrmix.authcenterback.dto.response.data.TokenResponse;
import com.fqrmix.authcenterback.dto.response.data.UserDataResponse;
import com.fqrmix.authcenterback.exception.UserAlreadyExistsException;
import com.fqrmix.authcenterback.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Period;
import java.util.Date;

@RestController
@CrossOrigin(
        origins = "https://fqrmix.ru,https://api.fqrmix.ru,https://auth.fqrmix.ru",
        maxAge = 3600,
        allowCredentials = "true"
)
@RequestMapping("/auth")
@Tag(name = "Аутентификация")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiSuccessResponseImpl<UserDataResponse>> register(
            @RequestBody
            @Valid
            RegisterRequestDTO request
    ) throws UserAlreadyExistsException {
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiSuccessResponseImpl.<UserDataResponse>builder()
                            .withType("success")
                            .withData(authenticationService.register(request))
                            .build());
        } catch (Exception e) {
            log.error(
                    "The registration process was interrupted by an exception: {}",
                    (Object) e.getStackTrace()
            );
            throw e;
        }
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponseImpl<TokenResponse>> login(
            @RequestBody
            @Valid
            LoginRequestDTO request
    ) throws AuthenticationException {
        try {
            TokenResponse response = authenticationService.login(request);

            ResponseCookie accessTokenCookie = ResponseCookie.from("_accessToken", response.getToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge((response.getExpirationDate().getTime() - new Date().getTime()) / 1000)
                    .domain(".fqrmix.ru")
                    .sameSite("None")
                    .build();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.SET_COOKIE)
                    .body(ApiSuccessResponseImpl.<TokenResponse>builder()
                                .withType("success")
                                .withData(response)
                                .build());
        } catch (Exception e) {
            log.error(
                    "The auth process was interrupted by an exception: {}",
                    (Object) e.getStackTrace()
            );
            throw e;
        }
    }
}
