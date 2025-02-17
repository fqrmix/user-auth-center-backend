package com.fqrmix.authcenterback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO for user authentication.
 */
@Data
@Schema(description = "Запрос на авторизацию пользователя")
public class LoginRequestDTO {

    @Schema(description = "Имя пользователя", example = "Johnny")
    @Size(min = 5, max = 20, message = "Имя пользователя должно содержать от 5 до 20 символов")
    @NotBlank
    private String username;

    @Schema(description = "Пароль пользователя", example = "myPasswordQWERTY123")
    @Size(min = 6, max = 120, message = "Длина пароля должна быть не более 120 символов")
    @NotBlank
    private String password;

}
