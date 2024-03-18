package com.fqrmix.authcenterback.dto.response.api.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fqrmix.authcenterback.dto.response.api.ApiResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse implements ApiResponse {
    private String type;
    private String message;
    private String code;
    private List errors;
    private String error;
}
