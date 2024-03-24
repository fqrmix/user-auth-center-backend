package com.fqrmix.authcenterback.dto.response.api.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fqrmix.authcenterback.dto.response.api.ErrorResponse;
import com.fqrmix.authcenterback.models.ErrorObject;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponseImpl implements ErrorResponse {
    private String type;
    private String message;
    private String code;
    private List<ErrorObject> errors;
}
