package com.fqrmix.authcenterback.dto.response.api.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fqrmix.authcenterback.dto.response.api.SuccessResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiSuccessResponseImpl<T> implements SuccessResponse<T> {
    private String type;
    private String message;
    private String code;
    private T data;
}
