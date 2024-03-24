package com.fqrmix.authcenterback.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorize")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
@Slf4j
public class ExampleController {
    @PostMapping
    @Operation(summary = "Доступен только авторизованным пользователям")
    public String authorize() {
        log.info(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities().toString()
        );
        return "Hello, world!";
    }
}
