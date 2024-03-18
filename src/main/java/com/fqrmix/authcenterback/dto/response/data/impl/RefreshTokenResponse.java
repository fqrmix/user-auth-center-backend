package com.fqrmix.authcenterback.dto.response.data.impl;

import com.fqrmix.authcenterback.dto.response.data.TokenResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ c refresh-токеном")
public class RefreshTokenResponse implements TokenResponse {
    @Schema(description = "Refresh-токен", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String refreshToken;

    @Schema(description = "Тип Refresh-токена", example = "UUID4")
    private String type;

}
