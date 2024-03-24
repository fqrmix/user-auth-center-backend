package com.fqrmix.authcenterback.dto.request;

import com.fqrmix.authcenterback.models.enums.ERole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Request DTO for user authorization.
 */
@Data
@Schema(description = "Запрос на авторизацию пользователя")
public class AuthorityRequestDTO {

    @Schema(description = "Запрашиваемая роль", example = "ADMIN")
    private ERole requestedRole;

}