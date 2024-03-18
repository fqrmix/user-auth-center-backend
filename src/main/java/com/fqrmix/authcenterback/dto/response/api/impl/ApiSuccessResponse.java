package com.fqrmix.authcenterback.dto.response.api.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fqrmix.authcenterback.dto.response.api.ApiResponse;
import com.fqrmix.authcenterback.dto.response.data.DataResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiSuccessResponse implements ApiResponse {

    private String type;
    private String message;
    private String code;
    private DataResponse data;

}
