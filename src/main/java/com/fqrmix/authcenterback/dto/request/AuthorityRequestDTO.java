package com.fqrmix.authcenterback.dto.request;

import com.fqrmix.authcenterback.models.enums.ERole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for user authorization.
 */
@Data
@Schema(description = "Запрос на авторизацию пользователя")
public class AuthorityRequestDTO {

    @Schema(description = "Запрашиваемая роль", example = "ROLE_ADMIN")
    @NotNull
    private ERole requestedRole;

}