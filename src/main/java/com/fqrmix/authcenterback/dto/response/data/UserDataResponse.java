package com.fqrmix.authcenterback.dto.response.data;

import com.fqrmix.authcenterback.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с данными пользователя")
public class UserDataResponse {
    @Schema(description = "Имя пользователя", example = "Johnny")
    private String username;

    @Schema(description = "Список ролей пользователя", example = "[ROLE_ADMIN, ROLE_USER]")
    private Set<Role> roles;

}
