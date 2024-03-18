package com.fqrmix.authcenterback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * Request DTO for user registration.
 */
@Data
@Schema(description = "Запрос на регистрацию пользователя")
public class RegisterRequestDTO {

    @Schema(description = "Имя пользователя", example = "Johnny")
    @Size(min = 5, max = 20, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    private Set<String> services;

    @Schema(description = "Пароль пользователя", example = "myPasswordQWERTY123")
    @Size(min = 6, max = 120, message = "Длина пароля должна быть от 6 до 120 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
