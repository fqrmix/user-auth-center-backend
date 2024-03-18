package com.fqrmix.authcenterback.controllers;

import com.fqrmix.authcenterback.dto.request.LoginRequestDTO;
import com.fqrmix.authcenterback.dto.request.RegisterRequestDTO;
import com.fqrmix.authcenterback.dto.response.api.ApiResponse;
import com.fqrmix.authcenterback.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@Tag(name = "Аутентификация")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @RequestBody
            @Valid
            RegisterRequestDTO request
    ) throws RuntimeException {
        try {
            return ResponseEntity.ok(
                    (ApiResponse) authenticationService.register(request)
            );
        } catch (Exception e) {
            log.error("The registration process was interrupted by an exception: {}", (Object) e.getStackTrace());
            throw e;
        }
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @RequestBody
            @Valid
            LoginRequestDTO request
    ) throws Exception {
        try {
            return ResponseEntity.ok(
                    (ApiResponse) authenticationService.login(request)
            );
        } catch (Exception e) {
            log.error("The auth process was interrupted by an exception: {}", (Object) e.getStackTrace());
            throw e;
        }
    }
}
