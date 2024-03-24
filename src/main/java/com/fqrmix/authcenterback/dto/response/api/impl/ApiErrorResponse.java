package com.fqrmix.authcenterback.dto.response.api.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fqrmix.authcenterback.dto.response.api.IErrorResponse;
import com.fqrmix.authcenterback.dto.response.api.ErrorObject;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse implements IErrorResponse {
    private String type;
    private String message;
    private String code;
    private List<ErrorObject> errors;
}
