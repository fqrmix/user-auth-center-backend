package com.fqrmix.authcenterback.dto.response.data.impl;

import com.fqrmix.authcenterback.dto.response.data.DataResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект с данными о токене")
public class TokenResponse implements DataResponse {
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String token;

    @Schema(description = "Тип токена", example = "JWT")
    private String type;

    @Schema(description = "Дата истечения срока жизни токена", example = "2024-03-20T19:23:01.000+00:00")
    private Date expirationDate;

}
