package com.fqrmix.authcenterback.controllers;

import com.fqrmix.authcenterback.dto.request.LoginRequestDTO;
import com.fqrmix.authcenterback.dto.request.RegisterRequestDTO;
import com.fqrmix.authcenterback.dto.response.api.ApiResponse;
import com.fqrmix.authcenterback.dto.response.api.impl.ApiSuccessResponse;
import com.fqrmix.authcenterback.dto.response.data.DataResponse;
import com.fqrmix.authcenterback.dto.response.data.impl.TokenResponse;
import com.fqrmix.authcenterback.dto.response.data.impl.UserDataResponse;
import com.fqrmix.authcenterback.exception.UserAlreadyExistsException;
import com.fqrmix.authcenterback.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiSuccessResponse<UserDataResponse>> register(
            @RequestBody
            @Valid
            RegisterRequestDTO request
    ) throws UserAlreadyExistsException {
        try {
            return ResponseEntity.ok()
                    .body(ApiSuccessResponse.<UserDataResponse>builder()
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
    public ResponseEntity<ApiSuccessResponse<TokenResponse>> login(
            @RequestBody
            @Valid
            LoginRequestDTO request
    ) throws AuthenticationException {
        try {
            TokenResponse response = authenticationService.login(request);

            ResponseCookie accessTokenCookie = ResponseCookie.from("_accessToken", response.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(new Date().getTime() - response.getExpirationDate().getTime() / 1000)
                    .domain(".fqrmix.ru")
                    .sameSite("None")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.SET_COOKIE)
                    .body(ApiSuccessResponse.<TokenResponse>builder()
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
