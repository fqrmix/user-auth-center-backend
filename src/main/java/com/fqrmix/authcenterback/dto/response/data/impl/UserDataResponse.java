package com.fqrmix.authcenterback.dto.response.data.impl;

import com.fqrmix.authcenterback.dto.response.data.DataResponse;
import com.fqrmix.authcenterback.models.Service;
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
public class UserDataResponse implements DataResponse {
    @Schema(description = "Имя пользователя", example = "Johnny")
    private String username;

    @Schema(description = "Список ролей пользователя", example = "[ADMIN, USER]")
    private Set<Service> services;
}
