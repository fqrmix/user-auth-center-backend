package com.fqrmix.authcenterback.controllers;

import com.fqrmix.authcenterback.dto.request.AuthorityRequestDTO;
import com.fqrmix.authcenterback.dto.response.api.impl.ApiSuccessResponseImpl;
import com.fqrmix.authcenterback.dto.response.data.UserDataResponse;
import com.fqrmix.authcenterback.services.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorize")
@RequiredArgsConstructor
@Tag(name = "Авторизация")
@Slf4j
public class AuthorityController {
    @PostMapping
    @Operation(summary = "Доступен только авторизованным пользователям с ролью, указанной  в запросе")
    public ResponseEntity<ApiSuccessResponseImpl<UserDataResponse>> authorize(
            @RequestBody
            AuthorityRequestDTO authorityRequestDTO,
            SecurityContextHolderAwareRequestWrapper request
    ) {
        if (!request.isUserInRole(authorityRequestDTO.getRequestedRole().toString())) {
            throw new AccessDeniedException("Access Denied");
        } else {
            var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok().body(
                ApiSuccessResponseImpl.<UserDataResponse>builder()
                    .withType("success")
                    .withMessage("Access Granted")
                    .withData(UserDataResponse.builder()
                            .withUsername(user.getUsername())
                            .withRoles(user.getRoles())
                            .build())
                    .build());
        }
    }
}
