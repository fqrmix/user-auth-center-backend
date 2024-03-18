package com.fqrmix.authcenterback.dto.response.data.impl;

import com.fqrmix.authcenterback.dto.response.data.TokenResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Wither;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ JWT-аутентификации")
public class JwtAuthenticationResponse implements TokenResponse {
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String token;

    @Schema(description = "Тип токена", example = "Bearer")
    private String type;

}
